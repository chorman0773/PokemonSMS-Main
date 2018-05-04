package com.google.sites.clibonlineprogram.pokemonsms.net.client;

import com.google.sites.clibonlineprogram.pokemonsms.net.packet.IPacket;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;

public class CPacketBattleForfiet implements IPacket {
	private TextComponent msg;
	public CPacketBattleForfiet() {}
	public CPacketBattleForfiet(TextComponent msg) {
		this.msg = msg;
	}
	@Override
	public void encode(PacketBuffer buff) {
		// TODO Auto-generated method stub

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
		return true;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
