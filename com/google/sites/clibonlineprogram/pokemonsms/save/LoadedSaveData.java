package com.google.sites.clibonlineprogram.pokemonsms.save;

import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;

public class LoadedSaveData {
	private int version;
	private NBTTagCompound comp;
	private boolean isEncrypted;
	LoadedSaveData(NBTTagCompound comp,int version,boolean encrypted) {
		this.comp = comp;
		this.version = version;
		this.isEncrypted = encrypted;
	}
	public int getVersion() {
		return version;
	}
	public NBTTagCompound getData() {
		return comp;
	}

}
