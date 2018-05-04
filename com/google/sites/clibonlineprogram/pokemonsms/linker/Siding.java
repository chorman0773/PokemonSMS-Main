package com.google.sites.clibonlineprogram.pokemonsms.linker;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import com.google.sites.clibonlineprogram.pokemonsms.side.EnumSide;

public class Siding extends TwoArgFunction {
	public static class SidingIsSide extends OneArgFunction{
		private final EnumSide targetSide;
		public SidingIsSide(EnumSide side) {
			this.targetSide = side;
		}
		@Override
		public LuaValue call(LuaValue arg0) {
			// TODO Auto-generated method stub
			return EnumSide.getSide()==targetSide?LuaValue.TRUE:LuaValue.FALSE;
		}

	}
	public Siding() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public LuaValue call(LuaValue arg0, LuaValue arg1) {
		LuaTable t = new LuaTable();
		t.set("isSideServer", new SidingIsSide(EnumSide.SERVER));
		t.set("isSideClient", new SidingIsSide(EnumSide.CLIENT));
		return t;
	}

}
