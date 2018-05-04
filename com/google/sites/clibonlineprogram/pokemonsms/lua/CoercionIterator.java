package com.google.sites.clibonlineprogram.pokemonsms.lua;

import java.util.Iterator;

import org.luaj.vm2.LuaTable;

public class CoercionIterator<T> implements Iterator<T> {
	private final LuaTable t;
	private int idx  = 1;
	private final Class<T> cl;
	public CoercionIterator(LuaTable t,Class<T> cl) {
		this.t= t;
		this.cl = cl;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return idx<=t.length();
	}

	@Override
	public T next() {
		// TODO Auto-generated method stub
		return LuaJava.coerce(t.get(idx++), cl);
	}

	public void remove() {}

}
