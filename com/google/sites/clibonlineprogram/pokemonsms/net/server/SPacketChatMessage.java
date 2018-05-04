package com.google.sites.clibonlineprogram.pokemonsms.net.server;

import com.google.gson.JsonObject;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.IPacket;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;

public class SPacketChatMessage implements IPacket {

	private JsonObject sender;
	private String msg;
	private String channel;

	public SPacketChatMessage(JsonObject sender,String msg,String channel) {
		this.sender = sender;
		this.msg = msg;
		this.channel = channel;
	}

	@Override
	public void encode(PacketBuffer buff) {
		buff.writeJson(sender);
		buff.writeUTF(msg);
		buff.writeUTF(channel);
	}

	@Override
	public void decode(PacketBuffer buff) {
		sender = buff.readJson();
		msg = buff.readUTF();
		channel = buff.readUTF();

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
