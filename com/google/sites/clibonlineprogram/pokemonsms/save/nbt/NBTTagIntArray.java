package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class NBTTagIntArray extends NBTTagBase {
	private int[] payload;
	NBTTagIntArray() {
		// TODO Auto-generated constructor stub
	}
	public NBTTagIntArray(int[] payload) {
		this.payload = new int[payload.length];
		System.arraycopy(payload, 0, this.payload, 0, payload.length);
	}
	public NBTTagIntArray(int size) {
		this.payload = new int[size];
	}

	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		stream.writeInt(payload.length);
		for(int i = 0;i<payload.length;i++)
			stream.writeInt(payload[i]);
		sizeTracker.addAndGet(4+payload.length*4);
	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		int length = data.readInt();
		this.payload = new int[length];
		for(int i = 0;i<length;i++)
			this.payload[0] = data.readInt();
		sizeTracker.addAndGet(4+payload.length*4);
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_INT_ARRAY;
	}


	public void set(int i,int b) {
		payload[i] = b;
	}
	public int get(int i) {
		return payload[i];
	}

}
