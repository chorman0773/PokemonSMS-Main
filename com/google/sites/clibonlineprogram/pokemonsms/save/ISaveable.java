package com.google.sites.clibonlineprogram.pokemonsms.save;


import java.time.Duration;
import java.time.Instant;

import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaDouble;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTConstants;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTPrimitive;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagBase;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagByte;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagDouble;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagInt;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagString;

/**
 * Describes an Object which can be saved to an NBTTagCompound.<br/>
 * This class also provides several utilities for writing Other types to NBT, such as Lua Values.<br/>
 *
 * @author Connor Horman
 *
 */
public interface ISaveable extends NBTConstants {
	public void save(NBTTagCompound comp);
	public void load(NBTTagCompound comp);

	/**
	 * Writes a Lua Object. Note that this is only useful for numbers, strings, and tables which map strings to these values.
	 * Recursive Tables are not supported.
	 * @param v a value to write to NBT
	 * @return an NBTTag which represents the value.
	 */
	static NBTTagBase writeLuaObject(LuaValue v) {
		switch(v.type()) {
		case LuaValue.TBOOLEAN:
			return writeBoolean(v.toboolean()?LuaValue.TRUE:LuaValue.FALSE);
		case LuaValue.TNUMBER:
			return writeNumber(v.checknumber());
		case LuaValue.TINT:
			return writeNumber(v.checknumber());
		case LuaValue.TSTRING:
			return writeString(v.checkstring());
		case LuaValue.TTABLE:
			return writeTable(v.checktable());
		}
		throw new IllegalArgumentException("Lua Argument "+v+" of type "+v.typename()+" is not supported by writeLuaObject");
	}
	private static NBTTagBase writeBoolean(LuaBoolean b) {
		return new NBTTagByte((byte)(b.v?1:0));
	}
	private static NBTTagBase writeNumber(LuaNumber v) {
		if(v.isint())
			return new NBTTagInt(v.toint());
		else
			return new NBTTagDouble(v.todouble());
	}
	private static NBTTagBase writeString(LuaString s) {
		return new NBTTagString(s.tojstring());
	}
	private static NBTTagBase writeTable(LuaTable t) {
		NBTTagCompound comp = new NBTTagCompound();
		for(LuaValue s:t.keys())
			comp.setTag(s.checkjstring(),writeLuaObject(t.get(s)));
		return comp;
	}
	public static LuaValue readLua(NBTTagBase base) {
		switch(base.getTagType()) {
		case TAG_INT:
		case TAG_SHORT:
			return LuaInteger.valueOf(((NBTPrimitive)base).getInt());
		case TAG_LONG:
		case TAG_FLOAT:
		case TAG_DOUBLE:
			return LuaDouble.valueOf(((NBTPrimitive)base).getDouble());
		case TAG_BYTE:
			return LuaBoolean.valueOf(((NBTPrimitive)base).getByte()==1);
		case TAG_COMPOUND:
			return readTable((NBTTagCompound)base);
		}
		return LuaValue.NIL;
	}
	public static LuaTable readTable(NBTTagCompound comp) {
		LuaTable t = new LuaTable();
		for(String s:comp.keySet())
			t.set(s, readLua(comp.getTag(s)));
		return t;
	}
	public static NBTTagCompound writeInstant(Instant i) {
		NBTTagCompound ret = new NBTTagCompound();
		ret.setLong("Seconds", i.getEpochSecond());
		ret.setInteger("Nanos", i.getNano());
		return ret;
	}
	public static NBTTagCompound writeDuration(Duration i) {
		NBTTagCompound ret = new NBTTagCompound();
		ret.setLong("Seconds", i.getSeconds());
		ret.setInteger("Nanos", i.getNano());
		return ret;
	}
	public static Instant readInstant(NBTTagCompound comp) {
		long seconds = comp.getLong("Seconds");
		int nanos = comp.getInteger("Nanos");
		return Instant.ofEpochSecond(seconds, nanos);
	}
	public static Duration readDuration(NBTTagCompound comp) {
		long seconds = comp.getLong("Seconds");
		int nanos = comp.getInteger("Nanos");
		return Duration.ofSeconds(seconds, nanos);
	}
}
