package com.google.sites.clibonlineprogram.pokemonsms.net.server;

import com.google.gson.JsonObject;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.IPacket;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;

public class SPacketDisconnect implements IPacket {
	private JsonObject msg;
	public SPacketDisconnect() {
		// TODO Auto-generated constructor stub
	}
	public SPacketDisconnect(JsonObject msg) {
		this.msg = msg;
	}

	@Override
	public void encode(PacketBuffer buff) {
		buff.writeJson(msg);

	}

	@Override
	public void decode(PacketBuffer buff) {
		msg = buff.readJson();

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
		return 0xFD;
	}

}
