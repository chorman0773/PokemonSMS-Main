package com.google.sites.clibonlineprogram.pokemonsms.net.client;

import com.google.gson.JsonObject;
import com.google.sites.clibonlineprogram.pokemonsms.net.auth.SentryAccount;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.IPacket;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;
import com.google.sites.clibonlineprogram.pokemonsms.side.EnumSide;
import com.google.sites.clibonlineprogram.pokemonsms.util.Versions;

public class CPacketConnectServer implements IPacket {
	private TrainerInfo inf;
	private JsonObject acc;
	private String version;
	public CPacketConnectServer() {
		// TODO Auto-generated constructor stub
	}
	public CPacketConnectServer(TrainerInfo inf,JsonObject acc) {
		this.inf = inf;
		this.acc = acc;
		this.version = Versions.Strings.Current.NET;
	}

	@Override
	public void encode(PacketBuffer buff) {
		buff.writeJson(acc);
		buff.writeVersion(version);
		buff.writeJson(inf.toObject());
	}

	@Override
	public void decode(PacketBuffer buff) {
		acc = buff.readJson();
		version = buff.readVersion();
		inf = new TrainerInfo(buff.readJson());
	}

	@Override
	public boolean isRemote() {
		// TODO Auto-generated method stub
		return EnumSide.getSide()==EnumSide.SERVER;
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
