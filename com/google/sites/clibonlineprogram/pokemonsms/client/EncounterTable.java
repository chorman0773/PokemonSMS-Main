package com.google.sites.clibonlineprogram.pokemonsms.client;

import java.util.Random;

import org.luaj.vm2.LuaTable;

import com.google.sites.clibonlineprogram.pokemonsms.game.PlayerCharacter;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.Pokemon;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.Species;
import com.google.sites.clibonlineprogram.pokemonsms.util.registry.Registries;
import com.google.sites.clibonlineprogram.pokemonsms.util.registry.ResourceLocation;

public class EncounterTable {
	private WeightedRandomEncounterTableEntry[] entries;
	public static class WeightedRandomEncounterTableEntry{
		int weight;
		Species species;
		int level;
		public WeightedRandomEncounterTableEntry(LuaTable tbl) {
			this.weight = tbl.get("weight").checkint();
			this.species = Registries.speciesRegistry.getByName(new ResourceLocation(tbl.get("species").checkjstring()));
			this.level = tbl.get("level").checkint();
		}
	}

	public EncounterTable(LuaTable t) {
		int len = t.length();
		entries = new WeightedRandomEncounterTableEntry[len];
		for(int i = 1;i<=len;i++)
			entries[i] = new WeightedRandomEncounterTableEntry(t.get(i).checktable());

	}
	public Encounter doEncounter(Random r,PlayerCharacter t) {
		return null;
	}

}
