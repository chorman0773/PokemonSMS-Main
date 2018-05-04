package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NBTTagLongArray extends NBTTagBase {
	private long[] payload;
	NBTTagLongArray() {
		// TODO Auto-generated constructor stub
	}
	public NBTTagLongArray(long[] payload) {
		this.payload = new long[payload.length];
		System.arraycopy(payload, 0, this.payload, 0, payload.length);
	}

	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		stream.writeInt(payload.length);
		for(int i = 0;i<payload.length;i++)
			stream.writeLong(payload[i]);
		sizeTracker.addAndGet(4+payload.length*4);
	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		int length = data.readInt();
		this.payload = new long[length];
		for(int i = 0;i<length;i++)
			this.payload[0] = data.readLong();
		sizeTracker.addAndGet(4+payload.length*4);
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_LONG_ARRAY;
	}

	public void set(int i,long b) {
		payload[i] = b;
	}
	public long get(int i) {
		return payload[i];
	}

}
