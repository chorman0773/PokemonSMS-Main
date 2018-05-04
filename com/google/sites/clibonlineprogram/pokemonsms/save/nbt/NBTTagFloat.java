package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NBTTagFloat extends NBTPrimitive {
	private float underlying;
	public NBTTagFloat() {
		// TODO Auto-generated constructor stub
	}
	public NBTTagFloat(float val) {
		this.underlying = val;
	}

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return (int)underlying;
	}

	/* (non-Javadoc)
	 * @see com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTPrimitive#getFloat()
	 */
	@Override
	public float getFloat() {
		// TODO Auto-generated method stub
		return underlying;
	}

	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(4);
		stream.writeFloat(underlying);
	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(4);
		underlying = data.readFloat();
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_FLOAT;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(underlying);
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
		NBTTagFloat other = (NBTTagFloat) obj;
		if (Float.floatToIntBits(underlying) != Float.floatToIntBits(other.underlying))
			return false;
		return true;
	}

}
