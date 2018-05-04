package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;



public class NBTTagInt extends NBTPrimitive {
	private int stored;
	NBTTagInt() {
		// TODO Auto-generated constructor stub
	}
	public NBTTagInt(int i) {
		this.stored = i;
	}

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return stored;
	}

	@Override
	public void encode(DataOutput data,AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(4);
		data.writeInt(stored);
	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		sizeTracker.addAndGet(4);
		stored = data.readInt();
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_INT;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + stored;
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
		NBTTagInt other = (NBTTagInt) obj;
		if (stored != other.stored)
			return false;
		return true;
	}

}
