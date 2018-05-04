package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NBTTagByteArray extends NBTTagBase {
	private byte[] data;
	NBTTagByteArray() {
		// TODO Auto-generated constructor stub
	}
	public NBTTagByteArray(byte[] data) {
		this.data = new byte[data.length];
		System.arraycopy(data, 0, this.data, 0, data.length);
	}
	public NBTTagByteArray(int size) {
		data = new byte[size];
	}

	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		stream.writeInt(data.length);
		sizeTracker.addAndGet(4);
		sizeTracker.addAndGet(data.length);
		stream.write(data);

	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		int length = data.readInt();
		sizeTracker.addAndGet(4);
		sizeTracker.addAndGet(length);
		this.data = new byte[length];
		data.readFully(this.data);
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_BYTE_ARRAY;
	}

	public void set(int i,byte b) {
		data[i] = b;
	}
	public byte get(int i) {
		return data[i];
	}
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return data;
	}

}
