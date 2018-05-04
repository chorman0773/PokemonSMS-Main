package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.google.sites.clibonlineprogram.pokemonsms.util.functional.primitive.ByteSupplier;

public abstract class NBTTagBase implements NBTConstants {
	private static final List<Supplier<? extends NBTTagBase>> tagTypes;
	static {
		tagTypes = Arrays.asList(NBTTagEnd::new,NBTTagByte::new,NBTTagShort::new,NBTTagInt::new,NBTTagLong::new
				,NBTTagFloat::new,NBTTagDouble::new,NBTTagByteArray::new,NBTTagString::new,NBTTagList::new
				,NBTTagCompound::new,NBTTagIntArray::new,NBTTagLongArray::new);

	}
	NBTTagBase() {
		// TODO Auto-generated constructor stub
	}
	static String readString(DataInput stream,AtomicInteger sizeTracker) throws IOException {
		int size = stream.readUnsignedShort();
		sizeTracker.addAndGet(2+size);
		byte[] data = new byte[size];
		stream.readFully(data);
		return new String(data,StandardCharsets.UTF_8);
	}
	static void writeString(DataOutput stream,AtomicInteger sizeTracker,String name) throws IOException{
		byte[] data = name.getBytes(StandardCharsets.UTF_8);
		int len = data.length;
		assert len<(1<<16):"Name too long";
		sizeTracker.addAndGet(2+len);
		stream.writeShort(len);
		stream.write(data);
	}

	static NBTTagBase readFullTag(DataInput stream,AtomicInteger sizeTracker,AtomicReference<String> nameRef) throws IOException {
		int type = stream.readUnsignedByte();
		sizeTracker.addAndGet(1);
		if(type!=0)
			nameRef.set(readString(stream,sizeTracker));
		Supplier<? extends NBTTagBase> retriever = tagTypes.get(type);
		NBTTagBase ret = retriever.get();
		ret.decode(stream, sizeTracker);
		return ret;
	}
	static NBTTagBase readListTag(int type,DataInput stream,AtomicInteger sizeTracker) throws IOException{
		Supplier<? extends NBTTagBase> retriever = tagTypes.get(type);
		NBTTagBase ret = retriever.get();
		ret.decode(stream, sizeTracker);
		return ret;
	}
	public static NBTTagCompound readFromFile(DataInput strm) throws IOException{
		return (NBTTagCompound)readFullTag(strm,new AtomicInteger(),new AtomicReference<>());
	}
	public static void writeToFile(DataOutput strm,NBTTagCompound comp) throws IOException{
		writeFull(strm,new AtomicInteger(),"",comp);
	}

	static void writeFull(DataOutput stream,AtomicInteger sizeTracker,String name,NBTTagBase b) throws IOException{
		stream.write(b.getTagType());
		sizeTracker.addAndGet(1);
		if(b.getTagType()!=0)
			writeString(stream,sizeTracker,name);
		b.encode(stream, sizeTracker);
	}


	public abstract void encode(DataOutput stream,AtomicInteger sizeTracker)throws IOException;
	public abstract void decode(DataInput data,AtomicInteger sizeTracker)throws IOException;

	public abstract int getTagType();

	public int hashCode() {
		return getTagType();
	}

	public boolean equals(Object o) {
		if(o==null)
			return false;
		else if(o==this)
			return true;
		else if(!(o instanceof NBTTagBase))
			return false;
		NBTTagBase target = (NBTTagBase)o;
		return this.getTagType() == target.getTagType();
	}

}
