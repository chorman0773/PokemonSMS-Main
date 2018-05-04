package com.google.sites.clibonlineprogram.pokemonsms.pokemon.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import com.google.sites.clibonlineprogram.pokemonsms.events.EventBus;
import com.google.sites.clibonlineprogram.pokemonsms.resources.ResourcePath;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent.*;
import com.google.sites.clibonlineprogram.pokemonsms.lua.LuaJava;
import com.google.sites.clibonlineprogram.pokemonsms.util.registry.ResourceLocation;

public class Item {
	private EventBus bus;
	private TextComponent name;
	private TextComponent description;
	private ResourcePath imagePath;
	private ResourceLocation registryName;
	private boolean isEquipment;
	private boolean isKey;
	private LuaFunction getName_func;
	private LuaFunction getImage_func;
	private LuaFunction getDescription_func;
	private LuaFunction canPokemonUse_func;
	private LuaFunction canPokemonSteel_func;
	public Item(LuaTable t) {
		// TODO Auto-generated constructor stub
	}

	public static int getKeyItemCount() {
		return 0;
	}
	public static int getMachineItemCount() {
		return 0;
	}

	public ResourceLocation getRegistryName() {
		// TODO Auto-generated method stub
		return registryName;
	}
	private static TextComponent getArgumentComponentFromLua(LuaValue v,ItemStack src) {
		if(v.istable())
			return TextComponent.fromLua(v);
		else if(v.isfunction())
			return getArgumentComponentFromLua(v.call(LuaJava.coerce(src)),src);
		else if(v.isnil())
			return new TextComponentString("");
		else
			return new TextComponentString(v.tojstring());
	}

	public TextComponent getTranslatedName(ItemStack src) {
		if(getName_func!=null)
		{
			Varargs ret  = getName_func.call(LuaJava.coerce(src));
			if(ret.isnoneornil(2))
				return TextComponent.fromLua(ret.arg1()).format(new TextComponent[0], new AtomicInteger());
			else {
				TextComponent o = TextComponent.fromLua(ret.arg1());
				List<TextComponent> args = new ArrayList<>();
				for(int  i = 1;i<ret.narg();i++)
					args.add(getArgumentComponentFromLua(ret.arg(i),src));
				return o.format(args.stream().toArray(TextComponent[]::new), new AtomicInteger());
			}
		}else
			return name;
	}


}
