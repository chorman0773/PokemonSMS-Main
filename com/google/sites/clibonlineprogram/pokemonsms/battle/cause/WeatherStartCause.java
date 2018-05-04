package com.google.sites.clibonlineprogram.pokemonsms.battle.cause;

import com.google.sites.clibonlineprogram.pokemonsms.battle.ActivePokemon;

public interface WeatherStartCause {

	boolean isPokemonSource();
	ActivePokemon getPokemonSource();
	boolean isGameSource();


}
