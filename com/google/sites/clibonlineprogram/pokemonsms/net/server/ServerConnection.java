package com.google.sites.clibonlineprogram.pokemonsms.net.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.DestroyFailedException;

import com.google.sites.clibonlineprogram.StringUtil;
import com.google.sites.clibonlineprogram.pokemonsms.net.client.CPacketKeepAlive;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.IPacket;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;

public class ServerConnection implements AutoCloseable {
	private PublicKey serverPublic;
	private PrivateKey serverPrivate;
	private Key clientPublic;
	private byte[] directedMSG = new byte[2048];
	private byte[] interlacedMSG = new byte[4096];
	private byte[] msgDigest = new byte[256];
	private SecretKey sessionSymetric;
	private Socket sock;
	private DataInputStream nonSecureIn;
	private DataOutputStream nonSecureOut;
	private DataInputStream secureIn;
	private DataOutputStream secureOut;
	private static final SecureRandom rand = new SecureRandom();
	private static final MessageDigest sha256;
	private static final int MAGIC = 0x0F0E1C3E;
	private static final List<Supplier<IPacket>> packetsById = new ArrayList<>();
	private boolean isOpen;
	static {
		try {
			sha256 = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Could not Initialize the SHA256 Hash Provider");
		}
	}
	public ServerConnection(KeyPair keys,Socket s) {
		this.serverPublic = keys.getPublic();
		this.serverPrivate = keys.getPrivate();
	}
	private static byte[] interlace(byte[] a,byte[] b) {
		byte[] ret = new byte[4096];
		for(int i =0;i<2048;i++) {
			short curr = 0;
			for(int j=0;j<8;j++) {
				curr |=(a[i]&(1<<j*2+1)>>(j*2+1))|(b[i]&(1<<j*2)>>(j*2));
			}
			ret[2*i] = (byte) (curr>>8);
			ret[2*i+1] = (byte)curr;
		}
		return ret;
	}

	public void openConnection() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
		boolean finished = false;
		int numAttempts = 0;
		do{
			nonSecureIn = new DataInputStream(sock.getInputStream());
			nonSecureOut = new DataOutputStream(sock.getOutputStream());
			Cipher wrap = Cipher.getInstance("RSA");
			wrap.init(Cipher.WRAP_MODE, (Key) null);
			byte[] wrappedPublic = wrap.wrap(serverPublic);
			nonSecureOut.write(wrappedPublic);
			byte[] wrappedClientPublic = new byte[2048];
			nonSecureIn.read(wrappedClientPublic);
			Cipher unwrap = Cipher.getInstance("RSA");
			unwrap.init(Cipher.UNWRAP_MODE, (Key)null);
			clientPublic =  unwrap.unwrap(wrappedClientPublic, "RSA", Cipher.PUBLIC_KEY);
			Cipher encrypt = Cipher.getInstance("RSA");
			encrypt.init(Cipher.ENCRYPT_MODE, clientPublic);
			Cipher sign = Cipher.getInstance("RSA");
			sign.init(Cipher.ENCRYPT_MODE, serverPrivate);
			Cipher decrypt = Cipher.getInstance("RSA");
			decrypt.init(Cipher.DECRYPT_MODE, serverPrivate);
			Cipher unsign = Cipher.getInstance("RSA");
			CipherInputStream decryptionStream = new CipherInputStream(sock.getInputStream(),decrypt);
			secureIn = new DataInputStream(new CipherInputStream(decryptionStream,unsign));
			CipherOutputStream encryptionStream = new CipherOutputStream(sock.getOutputStream(),encrypt);
			secureOut = new DataOutputStream(new CipherOutputStream(encryptionStream,sign));
			rand.nextBytes(directedMSG);
			secureOut.write(directedMSG);
			byte[] clientMSG = new byte[2048];
			secureIn.read(clientMSG);
			interlacedMSG = interlace(directedMSG,clientMSG);
			msgDigest = sha256.digest(interlacedMSG);
			sessionSymetric = new SecretKeySpec(msgDigest,"AES");
			encrypt = Cipher.getInstance("AES");
			encrypt.init(Cipher.ENCRYPT_MODE, sessionSymetric);
			decrypt = Cipher.getInstance("AES");
			decrypt.init(Cipher.DECRYPT_MODE, sessionSymetric);
			secureIn = new DataInputStream(new CipherInputStream(sock.getInputStream(),decrypt));
			secureOut = new DataOutputStream(new CipherOutputStream(sock.getOutputStream(),encrypt));
			secureOut.writeInt(MAGIC);
			int readMagic = secureIn.readInt();
			if(readMagic!=MAGIC)
				nonSecureOut.writeByte(255);
			else
				nonSecureOut.writeByte(0);
			int response = nonSecureIn.readUnsignedByte();
			if(response==255) {
				numAttempts++;
				if(numAttempts>10)
					close();
				else
					continue;
				return;
			}
			else
				finished = true;

		}while(!finished);
		isOpen = true;
	}
	@Override
	public void close() throws IOException {
		Arrays.fill(directedMSG, (byte) -1);
		Arrays.fill(interlacedMSG, (byte)-1);
		Arrays.fill(msgDigest,(byte)-1);
		try {
			sessionSymetric.destroy();
		}catch(DestroyFailedException e) {
		}
		sock.close();
		isOpen = false;
	}


	public void sendPacket(IPacket packet) throws IOException {
		PacketBuffer packetBuff = new PacketBuffer();
		packet.encode(packetBuff);
		byte[] data = packetBuff.toByteArray();
		secureOut.write(packet.getId());
		secureOut.writeInt(data.length);
		secureOut.write(data);
	}
	public IPacket readPacket() throws IOException {
		int id = secureIn.read();
		int size = secureIn.readInt();
		byte[] data = new byte[size];
		secureIn.readFully(data);
		PacketBuffer buff = new PacketBuffer(data);
		Supplier<IPacket> packetSrc = packetsById.get(id&0xFF);
		IPacket ret =packetSrc.get();
		ret.decode(buff);
		return ret;
	}
	public InetAddress getAddress() {
		// TODO Auto-generated method stub
		return sock.getInetAddress();
	}

	public boolean hasWaitingPackets() throws IOException {
		if(!isOpen)
			return false;
		else
			return secureIn.available()>0;
	}

}
