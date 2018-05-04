package com.google.sites.clibonlineprogram.pokemonsms.save;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.google.sites.clibonlineprogram.pokemonsms.PokemonSmsGame;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagBase;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;

public class SaveFileLocker implements AutoCloseable {

	public static class SaveFileUnlocker extends LoadedSaveData{

		public static SaveFileUnlocker read(byte[] password,File targetFile) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
			FileInputStream in = new FileInputStream(targetFile);
			DataInputStream data = new DataInputStream(in);
			data.readInt();//Consume Magic Number
			byte[] iv = new byte[15];
			byte[] salt = new byte[256];
			byte[] saltedPass;
			int ver = data.readUnsignedShort();
			data.readFully(iv);
			data.readFully(salt);
			saltedPass = new byte[256+password.length];
			System.arraycopy(password, 0, saltedPass, 0, password.length);
			System.arraycopy(salt, 0, saltedPass, password.length, 256);
			byte[] hash = SHA256.digest(saltedPass);
			SecretKey aesKey = new SecretKeySpec(hash,"AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			IvParameterSpec spec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
			CipherInputStream strm = new CipherInputStream(in,cipher);
			data = new DataInputStream(strm);
			try {
				NBTTagCompound ret = NBTTagBase.readFromFile(data);
				in.close();
				return new SaveFileUnlocker(ret,ver);
			}catch(IOException e) {
				throw new IOException("Failed to open locked save file, incorrect password on stream");
			}

		}
		private SaveFileUnlocker(NBTTagCompound comp,int ver) {
			super(comp,ver,true);
		}


	}

	private static final SecureRandom rand = new SecureRandom();
	private static final MessageDigest SHA256;
	static {
		try {
			SHA256 = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Failed");
		}
	}

	public static SaveFileLocker newInstance(byte[] password,File targetFile) throws IOException, GeneralSecurityException {
		if(!PokemonSmsGame.canUseCrypto256())
			throw new IllegalStateException("Save file locking is not supported due to insufficient cryptography.");
		byte[] iv = new byte[15];
		byte[] salt = new byte[256];
		rand.nextBytes(iv);
		rand.nextBytes(salt);
		return new SaveFileLocker(iv,salt,password,targetFile);
	}

	public static boolean verifyEncrypted(File f) throws IOException {
		FileInputStream in = new FileInputStream(f);
		DataInputStream data = new DataInputStream(in);
		int i = data.readInt();
		data.close();
		return i==CRYPTO_MAGIC;
	}

	private byte[] iv;
	private byte[] password;
	private byte[] salt;
	private DataOutputStream stream;
	private ByteArrayOutputStream tmpData;
	private SecretKey aesKey;
	private Cipher cipher;
	public static final int CRYPTO_MAGIC = 0xb06792a7;

	private SaveFileLocker(byte[] iv,byte[] salt,byte[] password,File targetFile) throws IOException, GeneralSecurityException {
		this.iv = iv;
		this.password = new byte[256+password.length];
		System.arraycopy(password, 0, this.password, 0, password.length);
		System.arraycopy(salt, 0, this.password, password.length, 256);
		stream = new DataOutputStream(new FileOutputStream(targetFile));
		this.salt = salt;
		byte[] hash = SHA256.digest(this.password);
		aesKey = new SecretKeySpec(hash,"AES");
		cipher = Cipher.getInstance("AES/CBC/NoPadding");
		IvParameterSpec spec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
	}



	@Override
	public void close() throws IOException {
		stream.writeInt(CRYPTO_MAGIC);
		stream.writeShort(ShadeResources.SAVE_FILE_VERSION);
		stream.write(iv);
		stream.write(salt);
		tmpData.writeTo(stream);
	}

	public void writeSaveData(NBTTagCompound comp) throws IOException {

		NBTTagBase.writeToFile(new DataOutputStream(tmpData), comp);
		close();
	}


}
