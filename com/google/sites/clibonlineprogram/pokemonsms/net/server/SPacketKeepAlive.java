package com.google.sites.clibonlineprogram.pokemonsms.net.server;

import java.time.Instant;

import com.google.sites.clibonlineprogram.pokemonsms.net.packet.IPacket;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;

public class SPacketKeepAlive implements IPacket {
	private long id;
	private Instant time;
	private NetHandlerServer owner;

	private static long genKeepAliveId(long conId) {
		Instant ts = Instant.now();
		return (conId&(1<<24L-1) <<(1<<40L))|((ts.getEpochSecond()*1000000000+ts.getNano())&(1<<40L-1));
	}

	public SPacketKeepAlive() {}
	public SPacketKeepAlive(NetHandlerServer s) {
		this.owner = s;
	}
	@Override
	public void encode(PacketBuffer buff) {
		this.id = genKeepAliveId(owner.connectionId);

	}

	@Override
	public void decode(PacketBuffer buff) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRemote() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isServerbound() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
