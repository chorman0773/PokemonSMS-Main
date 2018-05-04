package com.google.sites.clibonlineprogram.pokemonsms.side;

public enum EnumSide {
	CLIENT, SERVER;
	public static EnumSide getSide() {
		try {
			Class.forName("com.google.sites.clibonlineprogram.pokemonsms.PokemonSmsGame");
			return CLIENT;
		}catch(ClassNotFoundException e) {
			return SERVER;
		}

	}


}
