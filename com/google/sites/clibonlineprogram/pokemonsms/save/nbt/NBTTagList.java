package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class NBTTagList extends NBTTagBase {
	private List<NBTTagBase> tags = new ArrayList<>();
	private int tagType;
	public NBTTagList() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		int length = tags.size();
		sizeTracker.addAndGet(5);
		stream.write(tagType);
		stream.writeInt(length);
		for(NBTTagBase tag:tags)
			tag.encode(stream, sizeTracker);
	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		tagType = data.readUnsignedByte();
		int length = data.readInt();
		sizeTracker.addAndGet(5);
		for(int i = 0;i<length;i++)
			tags.add(readListTag(tagType,data,sizeTracker));

	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_LIST;
	}

	public int getListTagType() {
		return tagType;
	}
	public int getSize() {
		return tags.size();
	}

	public NBTTagBase getTag(int i) {
		return tags.get(i);
	}
	public void forEach(Consumer<? super NBTTagBase> apply) {
		tags.forEach(apply);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + tagType;
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
		NBTTagList other = (NBTTagList) obj;
		if (tagType != other.tagType)
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

}
