package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NBTTagDouble extends NBTPrimitive {
	private double val;
	public NBTTagDouble() {
		// TODO Auto-generated constructor stub
	}
	public NBTTagDouble(double val) {
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
		return (long)val;
	}
	/* (non-Javadoc)
	 * @see com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTPrimitive#getFloat()
	 */
	@Override
	public float getFloat() {
		// TODO Auto-generated method stub
		return (float)val;
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
		sizeTracker.addAndGet(4);
		stream.writeDouble(val);

	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(4);
		val = data.readDouble();

	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_DOUBLE;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(val);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		NBTTagDouble other = (NBTTagDouble) obj;
		if (Double.doubleToLongBits(val) != Double.doubleToLongBits(other.val))
			return false;
		return true;
	}

}
