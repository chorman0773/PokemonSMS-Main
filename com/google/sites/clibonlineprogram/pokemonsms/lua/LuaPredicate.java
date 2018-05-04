package com.google.sites.clibonlineprogram.pokemonsms.lua;

import java.util.function.Predicate;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LuaPredicate {

	private LuaPredicate() {
		// TODO Auto-generated constructor stub
	}

	public static <T> Predicate<T> predicateOf(LuaFunction f){
		return t->f.invoke(CoerceJavaToLua.coerce(t)).checkboolean(1);
	}

}
