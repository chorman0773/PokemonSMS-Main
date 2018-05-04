package com.google.sites.clibonlineprogram.pokemonsms.battle.condition;

public class Weather {
	private EnumWeatherType type;
	private boolean extreme;
	private int timer;
	public Weather(EnumWeatherType type,boolean extreme,int timer) {
		this.type = type;
		this.extreme = extreme;
		this.timer = timer;
	}

	public boolean isExtreme() {
		return extreme;
	}

	public int getTimer() {
		return timer;
	}
	public void tickTurn() {
		if(!extreme)
			timer--;
	}
	public boolean shouldRevert() {
		return (!extreme)&&timer<0;
	}

	public EnumWeatherType getBase() {
		return type;
	}

}
