package com.google.sites.clibonlineprogram.pokemonsms.pokemon;

import java.util.Map;

import org.luaj.vm2.LuaTable;

import com.google.sites.clibonlineprogram.pokemonsms.pokemon.move.Move;

public class Species {

	private String unLocalizedName;
	private byte genderThreshold;

	public static class FormData{
	Type type1;
	Type type2;
	Ability ability;
	boolean isPersistant;
	int[] baseStats;
	}
	private Move[] startMoves;
	private Move[] evolveMoves;
	private Map<Integer,Move[]> learnset;
	private Move[] learnableTms;
	private LuaTable eventHandler;
	private FormData base;
	private FormData[] forms;
	private int megaFormIndex;
	private int id;
	private boolean shinyLocked;


	public boolean hasMegaEvolution() {
		return megaFormIndex>0;
	}


	public Species(int id,LuaTable base) {

	}

	public boolean canBeShiny() {
		return !shinyLocked;
	}

	public int[] getBaseStats(int form) {
		return forms[form].baseStats;
	}

}
