package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NBTTagShort extends NBTPrimitive {
	private short val;
	public NBTTagShort(short val) {
		this.val = val;
	}
	public NBTTagShort() {

	}

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return val;
	}



	/* (non-Javadoc)
	 * @see com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTPrimitive#getShort()
	 */
	@Override
	public short getShort() {
		// TODO Auto-generated method stub
		return val;
	}
	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(2);
		stream.writeShort(val);

	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(2);
		val = data.readShort();
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_SHORT;
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
		NBTTagShort other = (NBTTagShort) obj;
		if (val != other.val)
			return false;
		return true;
	}

}
