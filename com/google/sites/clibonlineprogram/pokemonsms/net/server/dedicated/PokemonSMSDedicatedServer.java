package com.google.sites.clibonlineprogram.pokemonsms.net.server.dedicated;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ServerSocketFactory;

import com.google.sites.clibonlineprogram.pokemonsms.net.server.NetHandlerServer;
import com.google.sites.clibonlineprogram.pokemonsms.net.server.ServerConnection;

public class PokemonSMSDedicatedServer {
	private ServerSocket s;
	private KeyPair keyPair;
	private Map<Long,NetHandlerServer> activeHandlers = new TreeMap<>();
	private class ConnectionDaemon extends Thread{

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {

			while(true) {
				try(Socket sock = s.accept()) {
					ServerConnection sc = new ServerConnection(keyPair,sock);
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
	private static final int SERVER_PORT = 20002;

	public PokemonSMSDedicatedServer() throws Exception {
			keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
			s = ServerSocketFactory.getDefault().createServerSocket();
			s.bind(new InetSocketAddress(InetAddress.getLocalHost(),SERVER_PORT));
			new ConnectionDaemon().start();
	}
}
