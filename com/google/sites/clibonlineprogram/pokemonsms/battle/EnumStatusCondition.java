package com.google.sites.clibonlineprogram.pokemonsms.battle;

public enum EnumStatusCondition {
	OK ,POISON, BURN, PARALYZE, SLEEP, FREEZE;

	public static EnumStatusCondition ofNetworkEnum(int status) {
		switch(status) {
		case 0:
			return OK;

		case 1:
		case 2:
			return POISON;

		case 3:
			return BURN;
		case 4:
			return PARALYZE;
		case 5:
			return SLEEP;
		case 6:
			return FREEZE;
		}
		throw new IllegalArgumentException("Invalid Enum Value "+status);
	}
}
