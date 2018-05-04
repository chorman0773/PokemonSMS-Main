package com.google.sites.clibonlineprogram.pokemonsms.pokemon.move;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import com.google.sites.clibonlineprogram.pokemonsms.events.EventBus;
import com.google.sites.clibonlineprogram.pokemonsms.lua.LuaJava;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.Type;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;

import com.google.sites.clibonlineprogram.pokemonsms.util.registry.ResourceLocation;

public class Move {
	private final ResourceLocation loc;
	private final int power;
	private final int targeting;
	private final Type type;
	private final MoveCategory category;
	private final int pp;
	private final EventBus bus;
	private final Set<String> traits;
	private final double accuracy;
	private final TextComponent name;
	private final TextComponent description;


	public Move(LuaTable t) {
		this.loc = new ResourceLocation(t.get("loc").checkjstring());
		this.power = t.get("power").optint(-1);
		this.targeting = t.get("targeting").checkint();
		this.type = LuaJava.coerce(t.get("type"), Type.class);
		this.category = LuaJava.coerce(t.get("category"), MoveCategory.class);
		this.pp  = t.get("pp").checkint();
		this.bus = new EventBus(t.get("eventBus").checktable());
		this.accuracy = t.get("accuracy").checkdouble();
		this.name  = TextComponent.fromLua(t.get("name"));
		this.description = TextComponent.fromLua(t.get("description"));
		this.traits = Collections.unmodifiableSet(LuaJava.toCoercedStream(t.get("traits").opttable(LuaValue.tableOf()), String.class).collect(TreeSet::new, Set::add, Set::addAll));
	}


	/**
	 * @return the loc
	 */
	public ResourceLocation getLoc() {
		return loc;
	}


	/**
	 * @return the power
	 */
	public int getPower() {
		return power;
	}


	/**
	 * @return the targeting
	 */
	public int getTargeting() {
		return targeting;
	}


	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}


	/**
	 * @return the category
	 */
	public MoveCategory getCategory() {
		return category;
	}


	/**
	 * @return the pp
	 */
	public int getPp() {
		return pp;
	}


	/**
	 * @return the bus
	 */
	public EventBus getEventBus() {
		return bus;
	}


	/**
	 * @return the accuracy
	 */
	public double getAccuracy() {
		return accuracy;
	}

	public boolean hasTrait(String trait) {
		return traits.contains(trait);
	}


	/**
	 * @return the name
	 */
	public TextComponent getName() {
		return name;
	}


	/**
	 * @return the description
	 */
	public TextComponent getDescription() {
		return description;
	}

}
