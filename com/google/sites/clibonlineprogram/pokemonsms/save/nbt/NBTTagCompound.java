package com.google.sites.clibonlineprogram.pokemonsms.save.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.BitSet;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NBTTagCompound extends NBTTagBase implements Map<String,NBTTagBase> {
	private Map<String,NBTTagBase> underlying = new TreeMap<>();
	public NBTTagCompound() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void encode(DataOutput stream, AtomicInteger sizeTracker) throws IOException {
		for(String name:underlying.keySet())
			NBTTagBase.writeFull(stream, sizeTracker, name,underlying.get(name));
		NBTTagBase.writeFull(stream, sizeTracker, "", new NBTTagEnd());
	}

	@Override
	public void decode(DataInput data, AtomicInteger sizeTracker) throws IOException {
		NBTTagBase tag;
		do {
			AtomicReference<String> name = new AtomicReference<>();
			tag = readFullTag(data,sizeTracker,name);
			if(!(tag instanceof NBTTagEnd))
				underlying.put(name.get(), tag);
		}while(!(tag instanceof NBTTagEnd));
	}

	@Override
	public int getTagType() {
		// TODO Auto-generated method stub
		return TAG_COMPOUND;
	}

	public boolean hasTag(String name) {
		return underlying.containsKey(name);
	}
	public boolean hasTag(String name,int type) {
		if(!hasTag(name))
			return false;
		NBTTagBase tag = underlying.get(name);
		if(type==TAG_ANY_NUMERIC)
			return tag instanceof NBTPrimitive;
		else
			return tag.getTagType()==type;
	}

	public NBTTagCompound getTagCompound(String name) {
		if(!hasTag(name,TAG_COMPOUND)) {
			NBTTagCompound ret = new NBTTagCompound();
			if(!hasTag(name))
				underlying.put(name, ret);
			return ret;
		}else
			return (NBTTagCompound)underlying.get(name);
	}
	public void setTag(String name,NBTTagBase tag) {
		underlying.put(name, tag);
	}
	public void setString(String name,String value) {
		setTag(name,new NBTTagString(value));
	}
	public String getString(String name) {
		if(!hasTag(name,TAG_STRING))
			return "";
		else
			return ((NBTTagString)getTag(name)).getString();

	}
	public NBTTagBase getTag(String name) {
		// TODO Auto-generated method stub
		return underlying.get(name);
	}

	public void setInteger(String name,int value) {
		setTag(name,new NBTTagInt(value));
	}
	public int getInteger(String name) {
		if(!hasTag(name,TAG_ANY_NUMERIC))
			return 0;
		else
			return ((NBTPrimitive)getTag(name)).getInt();
	}
	public void setLong(String name,long value) {
		setTag(name,new NBTTagLong(value));
	}
	public long getLong(String name) {
		if(!hasTag(name,TAG_ANY_NUMERIC))
			return 0;
		else
			return ((NBTPrimitive)getTag(name)).getLong();
	}

	public void setByteArray(String name,byte[] payload) {
		setTag(name,new NBTTagByteArray(payload));
	}
	private NBTPrimitive getPrimitive(String name) {

		return (NBTPrimitive)getTag(name);
	}


	public void setUUID(String name,UUID id) {
		setTag(name+"Most",new NBTTagLong(id.getMostSignificantBits()));
		setTag(name+"Least",new NBTTagLong(id.getLeastSignificantBits()));
	}
	public UUID getUUID(String name) {
		return new UUID(getLong(name+"Most"),getLong(name+"Least"));
	}
	public NBTTagList getTagList(String name) {
		if(!hasTag(name,TAG_LIST)){
				NBTTagList list = new NBTTagList();
				setTag(name,list);
				return list;
			}
		else
			return (NBTTagList)getTag(name);
	}

	@Override
	public void clear() {
		underlying.clear();

	}

	@Override
	public boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		return underlying.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub
		return underlying.containsValue(arg0);
	}

	@Override
	public Set<Entry<String, NBTTagBase>> entrySet() {
		// TODO Auto-generated method stub
		return underlying.entrySet();
	}

	@Override
	public NBTTagBase get(Object arg0) {
		// TODO Auto-generated method stub
		return underlying.get(arg0);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return underlying.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return underlying.keySet();
	}

	@Override
	public NBTTagBase put(String key, NBTTagBase value) {
		// TODO Auto-generated method stub
		return underlying.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends NBTTagBase> arg0) {
		underlying.putAll(arg0);

	}

	@Override
	public NBTTagBase remove(Object key) {
		// TODO Auto-generated method stub
		return underlying.remove(key);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return underlying.size();
	}

	@Override
	public Collection<NBTTagBase> values() {
		// TODO Auto-generated method stub
		return underlying.values();
	}

	public short getShort(String name) {
		if(!hasTag(name,TAG_ANY_NUMERIC))
			return 0;
		return getPrimitive(name).getShort();
	}
	public void setShort(String name,short s) {
		setTag(name,new NBTTagShort(s));
	}
	public boolean getBoolean(String name) {
		if(!hasTag(name,TAG_ANY_NUMERIC))
			return false;
		return getPrimitive(name).getByte()!=0;
	}
	public void setBoolean(String name,boolean b) {
		setTag(name,new NBTTagByte((byte) (b?1:0)));
	}

	public void setBitTable(String name,BitSet bits) {
		setTag(name,new NBTTagByteArray(bits.toByteArray()));
	}

	public BitSet getBitTable(String name) {
		if(!hasTag(name,TAG_BYTE_ARRAY))
			return new BitSet();
		else {
			byte[] b = ((NBTTagByteArray)getTag(name)).toByteArray();
			int size = b.length*8;
			BitSet ret = new BitSet(size);
			for(int i = 0;i<b.length;i++)
				for(int j=0;j<8;j++)
					if(((b[i]>>(8-j))&1)==1)
						ret.set(i*8+j);
			return ret;
		}
	}

}
