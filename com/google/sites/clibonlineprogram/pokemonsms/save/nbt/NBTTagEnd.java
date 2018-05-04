package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.sites.clibonlineprogram.pokemonsms.util.functional.primitive.ByteSupplier;

public final class NBTTagEnd extends NBTTagBase {

	public NBTTagEnd() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void encode(DataOutput data,AtomicInteger sizeTracker) {

	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_END;
	}

}
