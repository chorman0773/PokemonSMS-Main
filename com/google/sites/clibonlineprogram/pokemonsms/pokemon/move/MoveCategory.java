package com.google.sites.clibonlineprogram.pokemonsms.pokemon.move;

public enum MoveCategory {
	PHYSICAL,
	SPECIAL,
	STATUS;

	public boolean isOffensive() {
		return this!=STATUS;
	}
	public String getOffensiveStat() {
		if(!isOffensive())
			return null;
		if(this==PHYSICAL)
			return "atk";
		else
			return "special";
	}
}
