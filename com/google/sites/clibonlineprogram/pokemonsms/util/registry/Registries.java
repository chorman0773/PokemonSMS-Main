package com.google.sites.clibonlineprogram.pokemonsms.util.registry;



import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.google.sites.clibonlineprogram.pokemonsms.client.Map;
import com.google.sites.clibonlineprogram.pokemonsms.client.Sprite;
import com.google.sites.clibonlineprogram.pokemonsms.client.Tile;
import com.google.sites.clibonlineprogram.pokemonsms.linker.LuaContext;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.Ability;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.Species;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.item.Item;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.move.Move;
import com.google.sites.clibonlineprogram.pokemonsms.side.EnumSide;

public final class Registries extends TwoArgFunction {

	private Registries() {
		// TODO Auto-generated constructor stub
	}

	public static final Registry<Species> speciesRegistry = new Registry<>();
	public static final Registry<Ability> abilityRegistry = new Registry<>();
	public static final Registry<Move> moveRegistry = new Registry<>();
	public static final Registry<Item> itemRegistry = new Registry<>();

	public static class Client{
		public static final Registry<Tile> tileRegistry = new Registry<>();
		public static final Registry<Sprite> spriteRegistry = new Registry<>();
		public static final Registry<Map> locationRegistry = new Registry<>();

		public static LuaValue getClient() {
			LuaTable t = new LuaTable();


			return t;
		}

	}

	public static void loadAbilities() {
		LuaValue v = LuaContext.require("Abilities");
		LuaTable abilities = v.checktable();
		
		for(int i = 1;i<=abilities.length();i++) {
			Ability a = new Ability(abilities.get(i).checktable());
			abilityRegistry.register(a.loc, a);
		}
	}

	@Override
	public LuaValue call(LuaValue arg0, LuaValue arg1) {
		LuaTable t = new LuaTable();
		t.set("Species", speciesRegistry.luaV);
		t.set("Abilities", abilityRegistry.luaV);
		t.set("Moves", moveRegistry.luaV);
		t.set("Items", itemRegistry.luaV);
		if(EnumSide.getSide()==EnumSide.CLIENT)
			t.set("Client",Client.getClient());
		return t;
	}

}
