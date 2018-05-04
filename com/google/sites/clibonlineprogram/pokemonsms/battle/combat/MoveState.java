package com.google.sites.clibonlineprogram.pokemonsms.battle.combat;

import java.util.Map;
import java.util.TreeMap;

import com.google.sites.clibonlineprogram.pokemonsms.battle.ActivePokemon;
import com.google.sites.clibonlineprogram.pokemonsms.battle.Battle;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.move.Move;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.move.MoveCategory;

public class MoveState {
	private static final String[] specMods = {"critical","stab"};
	private ActivePokemon source;
	private ActivePokemon target;
	private Move move;
	private Battle battle;
	private Map<String,Boolean> specialModifiers;
	private MoveCategory cat;
	private int phase;
	private int base;
	private float lvlMod;
	private int attack;
	private int def;
	private float otherMods;
	private float specMod;
	private boolean areAbilitiesBlocked;
	private boolean applySecondaries;
	public MoveState(ActivePokemon source,ActivePokemon target,Move m,Battle b) {
		this.source = source;
		this.target = target;
		this.move = m;
		this.battle  =b;
		this.phase = 0;
	}

	public void start() {
		specialModifiers = new TreeMap<>();
		if(move!=null){

		}
	}






}
