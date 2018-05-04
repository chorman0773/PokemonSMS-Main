package com.google.sites.clibonlineprogram.pokemonsms.pokemon.item;

import org.luaj.vm2.LuaTable;

import com.google.sites.clibonlineprogram.pokemonsms.save.ISaveable;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;
import com.google.sites.clibonlineprogram.pokemonsms.util.registry.Registries;

public class ItemStack implements ISaveable {
	private LuaTable data;
	private int metadata;
	private Item base;
	private int count;
	public ItemStack(LuaTable t) {
		base = Registries.itemRegistry.getByName(t.get("item").checkjstring());
		metadata = t.get("metdata").checkint();
		data = t.get("extra").checktable();
		count = t.get("count").checkint();
	}
	public ItemStack(Item i) {
		this.base = i;
		this.count = 1;
	}
	public ItemStack(Item i,int count) {
		this.base = i;
		this.count = count;
	}
	public ItemStack(Item i,int count,int metadata) {
		this.base =  i;
		this.count = count;
		this.metadata = metadata;
	}
	public ItemStack(Item i,int count,int metadata,LuaTable t) {
		this.base =  i;
		this.count = count;
		this.metadata = metadata;
		this.data = t;
	}
	public ItemStack(NBTTagCompound comp) {
		load(comp);

	}
	@Override
	public void save(NBTTagCompound comp) {
		comp.setString("id", base.getRegistryName().toString());
		comp.setShort("metadata",(short) metadata);
		comp.setInteger("count", count);
		if(data!=null)
			comp.setTag("Extra", ISaveable.writeLuaObject(data));
	}
	@Override
	public void load(NBTTagCompound comp) {
		base = Registries.itemRegistry.getByName(comp.getString("id"));
		metadata = comp.getShort("metadata");
		count = comp.getInteger("count");
		if(comp.hasTag("Extra", TAG_COMPOUND))
			data = ISaveable.readTable(comp.getTagCompound("Extra"));

	}

}
