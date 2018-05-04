package com.google.sites.clibonlineprogram.pokemonsms.client.npc;

import org.luaj.vm2.LuaTable;

public class ShopItem {

	public ShopItem(LuaTable t) {
		LuaTable stack = t.get("item").checktable();
	}

}
