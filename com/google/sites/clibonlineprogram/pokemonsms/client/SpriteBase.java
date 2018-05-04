package com.google.sites.clibonlineprogram.pokemonsms.client;

import org.luaj.vm2.LuaTable;

import com.google.sites.clibonlineprogram.pokemonsms.linker.LuaContext;

public class SpriteBase {
	public static final SpriteBase[] sprites;
	static {
		LuaTable t = (LuaTable) LuaContext.require("Sprites");
		sprites = new SpriteBase[t.length()];
		for(int i = 0;i<t.length();i++)
			sprites[i] = new SpriteBase(t.get(i+1).checktable());
	}
	private LuaTable underlying;

	public SpriteBase(LuaTable t) {
		this.underlying = t;
	}

}
