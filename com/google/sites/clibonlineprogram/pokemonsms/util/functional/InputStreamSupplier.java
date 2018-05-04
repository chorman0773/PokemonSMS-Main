package com.google.sites.clibonlineprogram.pokemonsms.util.functional;

import java.io.IOException;
import java.io.InputStream;
import com.google.sites.clibonlineprogram.pokemonsms.util.functional.primitive.ByteSupplier;

public class InputStreamSupplier implements ByteSupplier {

	private InputStream in;

	public InputStreamSupplier(InputStream in) {
		this.in = in;
	}

	@Override
	public byte get() {
		try {
		return (byte) in.read();
		}catch(IOException e) {
			throw new RuntimeException("Can't read byte from stream",e);
		}
	}

}
