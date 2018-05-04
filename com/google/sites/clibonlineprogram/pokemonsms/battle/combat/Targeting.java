package com.google.sites.clibonlineprogram.pokemonsms.battle.combat;

/**
 * Provides constants for targeting pokemon in battle.
 * The Targeting constants can be used Lexically, for example to target Self (the move's user) OR an adjacent Ally use
 * SELF | ADJACENT|ALLY (read as Self or Adjacent Ally). The modifiers "Adjacent" and "All" do not represent seperate conditions (that are "or"d together),
 * but rather part of what it attaches to. This system is primarily for readbility, and has no affect on the actual use.
 * For example while Adjacent Ally or Enemy could be written as ADJACENT|ALLY|ENEMY, it can also be written as ALLY|ADJACENT|ENEMY, but there is no programatic distinction.
 * (It does not actually mean Ally or Adjacent Enemy, as bitflags are indisinguishable in the order they were added).
 * ADJACENT is Also correct (and serves the same result), as is ALL (which could be shorthand for ALL|ALLY|ENEMY).
 * When ALL is used, it should be noted that | between the qualifiers should be read as "and" and not "or".
 * ALLY&ENEMY is not actually correct and will result in 0.<br/><br/>
 * This class also provides 2 alias fields, to use when Plural specifiers are lexically correct. These fields (ALLIES and ENEMIES) are identical in value to
 * ALLY and ENEMY respectively.<br/>
 * In general, this interface should have its static fields imported to use them lexically.
 * @note This file is provided for convience of programming, as well as servering as a binding for the Constants.Targeting table. All fields provided are detailed in the specification of the Constants Binding as per the PokemonSMS Implementation Specification
 * @author Connor Horman
 *
 */
public interface Targeting {
	//Bitfields for Targeting.
	int SELF = 1, ADJACENT = 2, ALL = 4 , ALLY = 8, ENEMY = 16;

	//Lexical Aliases
	int ALLIES = ALLY, ENEMIES = ENEMY;

}
