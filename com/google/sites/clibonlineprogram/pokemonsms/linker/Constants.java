package com.google.sites.clibonlineprogram.pokemonsms.linker;



import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.google.sites.clibonlineprogram.pokemonsms.pokemon.Evolution.EvolutionType;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.Type;

public final class Constants extends TwoArgFunction {

	public final LuaTable TypeV = new LuaTable();
	public final LuaTable Evolution = new LuaTable();



	private Constants() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue v = new LuaTable();
		v.set("Types", TypeV);
		for(Type t:Type.values())
			TypeV.set(t.name(), CoerceJavaToLua.coerce(t));

		return v;
	}

}
