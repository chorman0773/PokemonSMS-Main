package com.google.sites.clibonlineprogram.pokemonsms.pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.luaj.vm2.LuaTable;

import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;
import com.google.sites.clibonlineprogram.pokemonsms.util.registry.ResourceLocation;

public class Ability {


	private LuaTable eventHandler;
	public final ResourceLocation loc;
	private TextComponent name;
	private TextComponent description;
	private List<ResourceLocation> lifetimeShared;

	public Ability(LuaTable t) {
		String loc = t.get("loc").checkjstring();
		this.loc = new ResourceLocation(loc);
		eventHandler = t.get("eventBus").checktable();
		name = TextComponent.fromLua(t.get("name"));
		description = TextComponent.fromLua(t.get("description"));
		if(t.get("lifetimeSharedAbilities").isnil())
			lifetimeShared = Collections.emptyList();
		else
		{
			List<ResourceLocation> list = new ArrayList<>();
			LuaTable val = t.get("lifetimeSharedAbilities").checktable();
			for(int i = 0;i<val.length();i++)
				list.add(new ResourceLocation(val.get(i+1).checkjstring()));
			lifetimeShared = Collections.unmodifiableList(list);
		}
	}

}
