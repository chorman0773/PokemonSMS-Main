package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NBTTagLong extends NBTPrimitive {
	private long val;
	public NBTTagLong() {
		// TODO Auto-generated constructor stub
	}
	public NBTTagLong(long val) {
		this.val = val;
	}

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return (int)val;
	}


	/* (non-Javadoc)
	 * @see com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTPrimitive#getLong()
	 */
	@Override
	public long getLong() {
		// TODO Auto-generated method stub
		return val;
	}
	/* (non-Javadoc)
	 * @see com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTPrimitive#getDouble()
	 */
	@Override
	public double getDouble() {
		// TODO Auto-generated method stub
		return val;
	}
	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(8);
		stream.writeLong(val);

	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(8);
		val = data.readLong();

	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_LONG;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (val ^ (val >>> 32));
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
		NBTTagLong other = (NBTTagLong) obj;
		if (val != other.val)
			return false;
		return true;
	}

}
