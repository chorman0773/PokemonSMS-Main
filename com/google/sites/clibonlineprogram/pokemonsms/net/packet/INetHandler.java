package com.google.sites.clibonlineprogram.pokemonsms.net.packet;

import java.net.InetAddress;

public interface INetHandler {

void recieve(IPacket packet);
/**
 * Called to send a packet to the given network service.
 * May be ignored for server based handlers that represent the entire server.
 * @param obj
 */
void send(IPacket obj);

boolean isRemote();
InetAddress getAddress();
}
