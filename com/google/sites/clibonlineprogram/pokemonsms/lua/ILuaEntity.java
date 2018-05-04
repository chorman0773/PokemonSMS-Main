package com.google.sites.clibonlineprogram.pokemonsms.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public interface ILuaEntity {
public Varargs call(String method,LuaValue... v);
public LuaValue get(String field);
}
