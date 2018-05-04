package com.google.sites.clibonlineprogram.pokemonsms.pokemon;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import static com.google.sites.clibonlineprogram.pokemonsms.pokemon.TypeMatchups.TypeMatchupCategory.*;
import static com.google.sites.clibonlineprogram.pokemonsms.pokemon.Type.*;
public final class TypeMatchups{

	public static enum TypeMatchupCategory{
		WEEKNESS, RESISTANCE, IMMUNITY;
	}


	private static final Map<Type,TypeMatchups> types = new TreeMap<>();

	private final Map<Type,TypeMatchupCategory> matchups = new TreeMap<>();

	static {
		types.put(NORMAL, new TypeMatchups(new Type[] {FIGHTING},new Type[] {},new Type[] {GHOST}));
		types.put(GRASS, new TypeMatchups(new Type[] {FIRE,ICE,POISON,BUG,FLYING},new Type[] {GRASS,WATER,GRASS,SOLAR}));
		types.put(FIRE, new TypeMatchups(new Type[] {WATER,ROCK,GROUND,SPATIAL},new Type[] {GRASS,FIRE,SOLAR,FAIRY},new Type[] {ICE}));
		types.put(WATER, new TypeMatchups(new Type[] {GRASS,ELECTRIC},new Type[] {WATER,STEEL,ICE,FIRE,SOLAR}));
		types.put(ELECTRIC, new TypeMatchups(new Type[] {GROUND},new Type[] {STEEL,ELECTRIC,FLYING}));
		types.put(FLYING, new TypeMatchups(new Type[] {ELECTRIC,ROCK,ICE,SOLAR},new Type[]{BUG,GRASS},new Type[] {GROUND}));
		types.put(FIGHTING, new TypeMatchups(new Type[] {FLYING,PSYCHIC,FAIRY,SPATIAL},new Type[] {BUG,DARK}));
		types.put(ROCK, new TypeMatchups(new Type[] {FIGHTING,GROUND,STEEL,WATER,GRASS,ELECTRIC},new Type[] {BUG,ROCK,FLYING,NORMAL}));
		types.put(GROUND, new TypeMatchups(new Type[] {WATER,ICE,GRASS},new Type[] {POISON}, new Type[] {ELECTRIC}));
		types.put(PSYCHIC, new TypeMatchups(new Type[] {GHOST,DARK,BUG,LUNAR},new Type[] {PSYCHIC,FIGHTING}));
		types.put(POISON, new TypeMatchups(new Type[] {GROUND,PSYCHIC}, new Type[] {POISON,GRASS,FIGHTING,FAIRY}));
		types.put(GHOST, new TypeMatchups(new Type[] {GHOST,DARK,SOLAR},new Type[] {POISON,BUG},new Type[] {NORMAL,FIGHTING}));
		types.put(STEEL, new TypeMatchups(new Type[] {FIRE,FIGHTING,GROUND},new Type[] {STEEL,ROCK,BUG,GRASS,PSYCHIC,ICE,DRAGON,FAIRY,FLYING},new Type[] {POISON,SOLAR,LUNAR,SPATIAL}));
		types.put(ICE, new TypeMatchups(new Type[] {FIRE,STEEL,FIGHTING,SOLAR},new Type[] {ICE}));
		types.put(BUG, new TypeMatchups(new Type[] {FIRE,FLYING,ROCK}, new Type[] {GRASS,GROUND,FIGHTING}));
		types.put(DARK, new TypeMatchups(new Type[] {BUG,FIGHTING,FAIRY}, new Type[] {DARK,GHOST},new Type[] {PSYCHIC}));
		types.put(DRAGON, new TypeMatchups(new Type[] {DRAGON,ICE,FAIRY},new Type[] {GRASS,FIRE,WATER,ELECTRIC}));
		types.put(FAIRY, new TypeMatchups(new Type[] {POISON,STEEL},new Type[] {DARK,FIGHTING},new Type[] {DRAGON}));
		types.put(SOLAR, new TypeMatchups(new Type[] {LUNAR,STEEL},new Type[] {FIRE,ELECTRIC,SOLAR},new Type[] {GROUND,SPATIAL}));
		types.put(LUNAR, new TypeMatchups(new Type[] {STEEL,FIGHTING,SPATIAL},new Type[] {NORMAL,FIRE,DARK,LUNAR},new Type[] {GROUND,SOLAR}));
		types.put(SPATIAL, new TypeMatchups(new Type[] {STEEL,PSYCHIC,DRAGON,SOLAR},new Type[] {WATER,FLYING,SPATIAL,DARK},new Type[] {NORMAL,FIRE,GROUND,FIGHTING,LUNAR}));
		types.put(TYPELESS, new TypeMatchups());
		types.put(CATASTROPHE, new TypeMatchups(new Type[0],new Type[0],new Type[] {CATASTROPHE}));
	}

	private TypeMatchups() {

	}
	private TypeMatchups(Type[]weekness) {
		for(Type t:weekness)
			matchups.put(t,WEEKNESS);

	}
	private TypeMatchups(Type[] weekness,Type[] resistance) {
		this(weekness);
		for(Type t:resistance)
			matchups.put(t, RESISTANCE);
	}
	private TypeMatchups(Type[] weekness,Type[] resistance,Type[] immunity) {
		this(weekness,resistance);
		for(Type t:immunity)
			matchups.put(t, IMMUNITY);
	}


	public static float getModifier(Type attacking,Type defending) {
		TypeMatchups matchups = types.get(defending);
		TypeMatchupCategory matchup = matchups.matchups.get(attacking);
		switch(matchup) {
		case WEEKNESS:
			return 2.0f;
		case RESISTANCE:
			return 0.5f;
		case IMMUNITY:
			return 0.0f;
		default:
			return 1.0f;
		}
	}

	public static float getTotalModifier(Type attacking,Type d0,Type d1) {
		return getModifier(attacking,d0)*getModifier(attacking,d1);
	}

	public static float getModifierIgnoringImmunity(Type attacking,Type defending) {
		TypeMatchups matchups = types.get(defending);
		TypeMatchupCategory matchup = matchups.matchups.get(attacking);
		switch(matchup) {
		case WEEKNESS:
			return 2.0f;
		case RESISTANCE:
			return 0.5f;
		default:
			return 1.0f;
		}
	}

	public static float getTotalModifierIgnoringImmunity(Type attacking,Type d0,Type d1) {
		return getModifier(attacking,d0)*getModifier(attacking,d1);
	}




}
