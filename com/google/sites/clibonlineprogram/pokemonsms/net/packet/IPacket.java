package com.google.sites.clibonlineprogram.pokemonsms.net.packet;

import com.google.gson.JsonObject;

public interface IPacket {
	void encode(PacketBuffer buff);
	void decode(PacketBuffer buff);

	boolean isRemote();
	boolean isServerbound();

	int getId();

}
