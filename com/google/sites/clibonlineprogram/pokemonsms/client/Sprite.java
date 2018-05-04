package com.google.sites.clibonlineprogram.pokemonsms.client;

import org.luaj.vm2.LuaTable;

import com.google.sites.clibonlineprogram.pokemonsms.save.ISaveable;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;

public class Sprite implements ISaveable {
	private SpriteBase base;
	private byte flags;
	private LuaTable data;
	public Sprite(LuaTable t) {

	}
	@Override
	public void save(NBTTagCompound comp) {
		// TODO Auto-generated method stub

	}
	@Override
	public void load(NBTTagCompound comp) {
		// TODO Auto-generated method stub

	}
}
