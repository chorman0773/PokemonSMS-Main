package com.google.sites.clibonlineprogram.pokemonsms.net.client;

import com.google.sites.clibonlineprogram.pokemonsms.net.packet.IPacket;
import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;

public class CPacketBattleAction implements IPacket {
	private int source;
	private int action;
	private byte[] targets;
	private int switchTarget;
	private boolean mega;
	public CPacketBattleAction() {}
	/**
	 * Constructor for creating an Action Packet for combat, with a single target.
	 * If the target is 16 then the target is the opponents side. If the target is 32 then the target is the user's side.
	 * If the target is -1 then the target is the entire field.
	 * @param source
	 * @param target
	 */
	public CPacketBattleAction(int source,int target) {
		if(target==-1)
			targets = new byte[0];
		else
			targets = new byte[] {(byte)target};
		this.source = source;
		this.action = 0;
	}

	public CPacketBattleAction(int source,int target,boolean mega) {
		if(target==-1)
			targets = new byte[0];
		else
			targets = new byte[] {(byte)target};
		this.source = source;
		this.action = 0;
		this.mega = mega;
	}
	public CPacketBattleAction(int source,byte[] targets) {
		this.targets = targets;
		this.source = source;
		this.action = 0;
	}
	public CPacketBattleAction(int source,byte[] targets,boolean mega) {
		this.targets = targets;
		this.source = source;
		this.action = 0;
		this.mega = false;
	}
	public CPacketBattleAction(boolean isSwitch,int source,int target) {
		if(isSwitch)
		{
			this.switchTarget = target;
			this.source = source;
			this.action = 1;
		}else
		{
			if(target==-1)
				targets = new byte[0];
			else
				targets = new byte[] {(byte)target};
			this.source = source;
			this.action = 0;
		}
	}

	@Override
	public void encode(PacketBuffer buff) {
		buff.writeByte(action);
		if(action == 0) {
			buff.writeInt(targets.length);
			buff.write(targets);
			buff.writeBoolean(mega);
		}else
			buff.write(switchTarget);
		buff.write(source);

	}

	@Override
	public void decode(PacketBuffer buff) {
		action = buff.readUnsignedByte();
		if(action == 0) {
			targets = new byte[buff.readInt()];
			buff.readFully(targets);
			mega = buff.readBoolean();
		}else
			switchTarget = buff.readUnsignedByte();
		source = buff.readUnsignedByte();

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
