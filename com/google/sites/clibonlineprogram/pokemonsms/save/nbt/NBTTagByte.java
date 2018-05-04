package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.sites.clibonlineprogram.pokemonsms.util.functional.primitive.ByteSupplier;

public class NBTTagByte extends NBTPrimitive {
	private byte val;
	NBTTagByte() {
		// TODO Auto-generated constructor stub
	}

	public NBTTagByte(byte b) {
		this.val = b;
	}

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return val;
	}

	/* (non-Javadoc)
	 * @see com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTPrimitive#getByte()
	 */
	@Override
	public byte getByte() {
		// TODO Auto-generated method stub
		return val;
	}

	@Override
	public void encode(DataOutput data,AtomicInteger sizeTracker) throws IOException {
		sizeTracker.incrementAndGet();
		data.writeByte(val);
	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.incrementAndGet();
		val = data.readByte();
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_BYTE;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + val;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NBTTagByte other = (NBTTagByte) obj;
		if (val != other.val)
			return false;
		return true;
	}

}
