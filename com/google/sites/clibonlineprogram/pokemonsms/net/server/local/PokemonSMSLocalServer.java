package com.google.sites.clibonlineprogram.pokemonsms.net.server.local;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ServerSocketFactory;

import com.google.sites.clibonlineprogram.pokemonsms.net.server.NetHandlerServer;
import com.google.sites.clibonlineprogram.pokemonsms.net.server.ServerConnection;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent.*;

public class PokemonSMSLocalServer implements LocalConnectionMessageIds,AutoCloseable {
	private static final int WAITING_PORT = 300;
	private static final int IN_GAME_PORT = 7500;
	private static final InetAddress WAITING_GROUP;
	private KeyPair keys;
	private MulticastSocket waitingSocket;
	private ServerSocket sSock;
	private LocalServerType t;
	private Map<Long,NetHandlerServer> activeHandlers = new TreeMap<>();
	private boolean isLookingForLocalUsers;
	private boolean isOpen;
	/**
	 * Specifies the kind of Local Server (For example
	 * @author Connor Horman
	 *
	 */
	public enum LocalServerType{
		JOIN_GAME,
		IRC,
		BATTLE,
		TRADE,
		BATTLE_AND_TRADE,
		BATTLE_AND_IRC,
		TRADE_AND_IRC,
		COMPLETE;
	}

	private class ConnectionDaemon extends Thread{

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			while(isOpen) {
				try(Socket sock = sSock.accept()) {
					ServerConnection sc = new ServerConnection(keys,sock);
					sc.openConnection();
					NetHandlerServer handle = new NetHandlerServer(sc);
					activeHandlers.put(handle.getID(), handle);
				} catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException e) {

				}
			}

		}

		/**
		 * Starts the daemon thread. The thread is
		 * {@inheritDoc}
		 * @see java.lang.Thread#start()
		 */
		@Override
		public synchronized void start() {
			this.setDaemon(true);
			super.start();
		}

	}
	private class WaitingDaemon extends Thread{

		/* (non-Javadoc)
		 * @see java.lang.Thread#start()
		 */
		@Override
		public synchronized void start() {
			this.setDaemon(true);
			super.start();
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while(isLookingForLocalUsers) {
				try {
				byte[] data = new byte[6];
				DatagramPacket loc = new DatagramPacket(data,6);
				waitingSocket.receive(loc);
				if(data[0]==LOOKUP)
				{
					byte[] addr = local.getAddress();
					data = new byte[6];
					data[0] = HOSTING;
					System.arraycopy(addr, 0, data, 1, addr.length);
					data[addr.length+1] = (byte)t.ordinal();
					DatagramPacket waitingPacket = new DatagramPacket(data, data.length);
					waitingSocket.send(waitingPacket);
				}
				}catch(IOException e) {

				}
			}
		}

	}

	static {
		try {
			WAITING_GROUP = InetAddress.getByName("224.0.2.60");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	public PokemonSMSLocalServer(LocalServerType t) throws IOException {
		InetAddress local = InetAddress.getLocalHost();
		sSock = ServerSocketFactory.getDefault().createServerSocket(IN_GAME_PORT);
		sSock.bind(new InetSocketAddress(InetAddress.getLocalHost(),IN_GAME_PORT));
		waitingSocket = new MulticastSocket(WAITING_PORT);
		waitingSocket.joinGroup(WAITING_GROUP);
		byte[] addr = local.getAddress();
		byte[] data = new byte[addr.length+2];
		data[0] = HELLO;
		System.arraycopy(addr, 0, data, 1, addr.length);
		data[addr.length+1] = (byte)t.ordinal();
		DatagramPacket waitingPacket = new DatagramPacket(data, data.length);
		waitingSocket.send(waitingPacket);
		isLookingForLocalUsers = true;
		new ConnectionDaemon().start();
		new WaitingDaemon().start();
	}
	@Override
	public void close() throws Exception {
		if(waitingSocket!=null)
			{
				byte[] addr = local.getAddress();
				byte[] data = new byte[addr.length+2];
				data[0] = GOODBYE;
				System.arraycopy(addr, 0, data, 1, addr.length);
				DatagramPacket goodbyePacket = new DatagramPacket(data, data.length);
				waitingSocket.send(goodbyePacket);
				isLookingForLocalUsers = false;
			}
		for(NetHandlerServer s:activeHandlers.values())
			s.kick(new TranslatebleTextComponent("server.connection.closed"));
		isOpen = false;
		sSock.close();
		keys.getPrivate().destroy();
	}
	private InetAddress local = InetAddress.getLocalHost();
}
