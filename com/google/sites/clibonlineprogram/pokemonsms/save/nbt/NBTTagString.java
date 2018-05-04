package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NBTTagString extends NBTTagBase {
	private String str;
	NBTTagString() {
		// TODO Auto-generated constructor stub
	}
	public NBTTagString(String str) {
		this.str = str;
	}

	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		NBTTagBase.writeString(stream, sizeTracker, str);

	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		str = readString(data,sizeTracker);
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_STRING;
	}
	public String getString() {
		return str;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((str == null) ? 0 : str.hashCode());
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
		NBTTagString other = (NBTTagString) obj;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
			return false;
		return true;
	}

}
