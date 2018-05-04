package com.google.sites.clibonlineprogram.pokemonsms.battle;

import com.google.gson.JsonObject;
import com.google.sites.clibonlineprogram.pokemonsms.PokemonSmsGame;

public class BattleRules {
	public enum BattleMode{
		SINGLE, DOUBLE, TRIPLE, LINK;

		public int getPokemonPerSide() {
			switch(this) {
			case SINGLE:
				return 1;
			case DOUBLE:
				return 2;
			case TRIPLE:
				return 3;
			case LINK:
				return 2;
			default:
				throw new IllegalStateException("Someone has messed with the universe");
			}
		}

	}
	private BattleMode mode;
	public BattleRules(JsonObject o) {
		this.mode = PokemonSmsGame.gson.fromJson(o.get("format"), BattleMode.class);
	}
	public BattleRules(BattleMode mode) {
		this.mode = mode;
	}
	public BattleMode getMode() {
		return mode;
	}

}
