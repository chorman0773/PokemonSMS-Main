package com.google.sites.clibonlineprogram.pokemonsms.pokemon.move;

import com.google.sites.clibonlineprogram.pokemonsms.save.ISaveable;
import com.google.sites.clibonlineprogram.pokemonsms.save.nbt.NBTTagCompound;

public class MoveInstance implements ISaveable {
	private Move m;
	private int remainingPP;
	private int numPPUps;
	public MoveInstance(Move m) {
		this.m = m;
		
	}
	public MoveInstance(){
		
	}

	@Override
	public void save(NBTTagCompound comp) {
		comp.setString("loc",m.getLoc().toString());
		comp.setInteger("RemainingPP",remaiingPP);
		comp.setInteger("NumPPUps",numPPUps);
	}

	@Override
	public void load(NBTTagCompound comp) {
		// TODO Auto-generated method stub

	}

}
