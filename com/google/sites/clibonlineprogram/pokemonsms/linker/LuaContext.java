package com.google.sites.clibonlineprogram.pokemonsms.linker;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LuaContext {
	public final static Globals _G = new CustomGlobals();
	static {
		LoadState.install(_G);
	}
	private LuaContext() {
		// TODO Auto-generated constructor stub
	}
	public static LuaValue require(String lib) {
		return _G.baselib.get("require").invoke(CoerceJavaToLua.coerce(lib)).arg1();
	}

}
