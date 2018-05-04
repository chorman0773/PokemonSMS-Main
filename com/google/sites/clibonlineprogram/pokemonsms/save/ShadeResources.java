package com.google.sites.clibonlineprogram.pokemonsms.save;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagBase;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;

public class ShadeResources {

	private ShadeResources() {
		// TODO Auto-generated constructor stub
	}
	public static final int SHADE_MAGIC = 0xEE001CAF;
	public static final int SAVE_FILE_VERSION = 0x0000;
	public static boolean verify(File f) throws IOException {
		DataInputStream strm = new DataInputStream(new FileInputStream(f));
		int i = strm.readInt();
		strm.close();
		return i==SHADE_MAGIC;
	}

	public static void save(NBTTagCompound comp,File f) throws IOException{
		try(DataOutputStream out = new DataOutputStream(new FileOutputStream(f))){
			out.writeInt(SHADE_MAGIC);
			out.writeShort(SAVE_FILE_VERSION);
			NBTTagBase.writeToFile(out, comp);
		}
	}

	public static LoadedSaveData load(File f)throws IOException{
		try(DataInputStream in = new DataInputStream(new FileInputStream(f))){
			in.readInt();
			int version = in.readUnsignedShort();
			return new LoadedSaveData(NBTTagBase.readFromFile(in),version,false);
		}
	}




}
