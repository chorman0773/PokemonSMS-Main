package com.google.sites.clibonlineprogram.pokemonsms.battle.turn;

import com.google.sites.clibonlineprogram.pokemonsms.battle.Battle;

public enum EnumAction {
COMBAT(0), SWITCH(1), ITEM(2), RUN(3);
	private final int actionId;
	EnumAction(int id){
		this.actionId = id;
	}

	public int getActionId() {
		return actionId;
	}

	public boolean isAvailable(Battle b) {
		return true;
	}
}
