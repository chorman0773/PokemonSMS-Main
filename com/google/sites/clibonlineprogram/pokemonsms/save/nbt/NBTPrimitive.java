package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

public abstract class NBTPrimitive extends NBTTagBase {

	public NBTPrimitive() {
		// TODO Auto-generated constructor stub
	}

	public abstract int getInt();
	public byte getByte() {
		return (byte)getInt();
	}
	public short getShort() {
		return (short)getInt();
	}
	public long getLong() {
		return getInt();
	}
	public float getFloat() {
		return (float)getInt();
	}
	public double getDouble() {
		return getInt();
	}

}
