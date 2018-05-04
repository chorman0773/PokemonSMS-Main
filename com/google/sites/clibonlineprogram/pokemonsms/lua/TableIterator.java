package com.google.sites.clibonlineprogram.pokemonsms.lua;

import java.util.Iterator;
import java.util.ListIterator;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

class TableIterator implements Iterator<LuaValue>, ListIterator<LuaValue> {
	private final LuaTable target;
	private int idx = 1;
	public TableIterator(LuaTable t) {
		this.target  = t;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return idx<=target.length();
	}

	@Override
	public LuaValue next() {
		// TODO Auto-generated method stub
		return target.get(idx);
	}

	@Override
	public void add(LuaValue val) {
		target.insert(idx, val);

	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return idx>1;
	}

	@Override
	public int nextIndex() {
		// TODO Auto-generated method stub
		return idx;
	}

	@Override
	public LuaValue previous() {
		// TODO Auto-generated method stub
		return target.get(--idx);
	}

	@Override
	public int previousIndex() {
		// TODO Auto-generated method stub
		return idx-1;
	}

	@Override
	public void set(LuaValue arg0) {
		target.set(idx, arg0);
	}

	public void remove() {
		target.remove(idx);
	}

}
