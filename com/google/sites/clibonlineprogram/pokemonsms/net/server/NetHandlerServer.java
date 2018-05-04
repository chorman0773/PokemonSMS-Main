package com.google.sites.clibonlineprogram.pokemonsms.net.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketOptions;
import java.nio.channels.SocketChannel;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.sites.clibonlineprogram.pokemonsms.net.client.CPacketKeepAlive;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.INetHandler;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.IPacket;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;

public final class NetHandlerServer implements INetHandler {
	private static long nextConId = System.currentTimeMillis();
	final long connectionId;
	private ServerConnection underlying;
	Instant lastSentKeepAliveTime;
	long lastKeepAliveId;
	private Instant lastRecievedKeepAlive;
	private long latency;
	private static final Duration KEEP_ALIVE_REQ =Duration.ofSeconds(2);
	private boolean isOpen = true;
	private static final long THREAD_TOTAL_TIME = 25;
	private boolean fireKeepAlive = true;
	private static final Map<Class<? extends IPacket>,BiConsumer<NetHandlerServer,? extends IPacket>> handlers = new HashMap<>();

	static {
		registerHandler(CPacketKeepAlive.class,NetHandlerServer::handleKeepAlive);
	}

	public static final <T extends IPacket> void registerHandler(Class<T> cl, BiConsumer<NetHandlerServer,T> handle) {
		if(handlers.containsKey(cl))
			throw new IllegalArgumentException("Handler already registered");
		else
			handlers.put(cl, handle);
	}

	private static Duration computeRequiredTime(long latency) {
		if(latency<250)
			return KEEP_ALIVE_REQ;
		else {
			long ms = (latency-250)*2;
			long nanos = ms*1000000;
			return Duration.ofSeconds(2, nanos);
		}
	}
	void handleKeepAlive(CPacketKeepAlive c) {

	}

	private class PacketDaemon extends Thread{

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
			while(isOpen) {
				try {
					Instant start = Instant.now();
					try {
					if(fireKeepAlive) {
						send(new SPacketKeepAlive(NetHandlerServer.this));
						fireKeepAlive = false;
					}
					if(Duration.between(lastRecievedKeepAlive, Instant.now()).compareTo(computeRequiredTime(latency))>0)
						kick(new TextComponent.TranslatebleTextComponent("server.network.kicktimedout"));
					else if(underlying.hasWaitingPackets())
						recieve(underlying.readPacket());
					}catch(IOException e) {

					}
					Duration time = Duration.between(start, Instant.now());
					long ms = time.get(ChronoUnit.MILLIS);
					int ns = time.getNano()%100000;
					Thread.sleep(ms, ns);
				}catch(InterruptedException e) {

				}
			}
		}

	}



	public NetHandlerServer(ServerConnection underlying) {
		this.connectionId = nextConId++;
		nextConId&=(1L<<48);
		this.underlying = underlying;
	}

	@Override
	public void recieve(IPacket packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(IPacket obj) {
		try {
			underlying.sendPacket(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isRemote() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public InetAddress getAddress() {
		// TODO Auto-generated method stub
		return underlying.getAddress();
	}
	public void kick(TextComponent reason) {
		SPacketDisconnect d = new SPacketDisconnect(reason.toJson());
		send(d);
		try {
			underlying.close();
			isOpen = false;
		}catch(Exception e) {

		}
	}

	public long getID() {
		// TODO Auto-generated method stub
		return connectionId;
	}

}
