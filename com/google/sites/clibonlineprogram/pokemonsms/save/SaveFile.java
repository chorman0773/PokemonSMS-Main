package com.google.sites.clibonlineprogram.pokemonsms.save;

import java.time.Duration;
import java.time.Instant;
import java.util.BitSet;

import com.google.sites.clibonlineprogram.pokemonsms.game.PlayerCharacter;
import com.google.sites.clibonlineprogram.pokemonsms.game.pc.Pc;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;

public class SaveFile implements ISaveable {
	private PlayerCharacter trainer;
	private Pc pc;
	private Duration totalPlayTime;
	private Instant loadTime;
	private String password;
	private int tasKeyFrame;
	private NBTTagCompound fileOptions;
	private int speedrunMode;

	public SaveFile() {
		// TODO Auto-generated constructor stub
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
