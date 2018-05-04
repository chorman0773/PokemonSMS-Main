package com.google.sites.clibonlineprogram.pokemonsms.util;

import java.util.Map.Entry;

import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaNil;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonUtils {

	private JsonUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static LuaValue jsonToLua(JsonElement e) {
		if(e.isJsonNull())
			return LuaNil.NIL;
		else if(e.isJsonObject()) {
			LuaTable t = new LuaTable();
			JsonObject o = e.getAsJsonObject();
			for(Entry<String,JsonElement> e1:o.entrySet()) 
				t.set(e1.getKey(), jsonToLua(e1.getValue()));
			return t;
		}else if(e.isJsonPrimitive()) {
			JsonPrimitive p = e.getAsJsonPrimitive();
			if(p.isBoolean())
				return p.getAsBoolean()?LuaBoolean.TRUE:LuaBoolean.FALSE;
			else if(p.isNumber())
				try {
					int i = p.getAsInt();
					return LuaInteger.valueOf(i);
				}catch(Exception ignored) {
					double d = p.getAsDouble();
					return LuaNumber.valueOf(d);
				}
			else
				return LuaString.valueOf(p.getAsString());
		}else
			return CoerceJavaToLua.coerce(e);
	}
	
	public static JsonElement luaToJson(LuaValue v) {
		switch(v.type()) {
		case LuaValue.TINT:
			int i = v.toint();
			JsonPrimitive j = new JsonPrimitive(i);
			return j;
		case LuaValue.TBOOLEAN:
			return new JsonPrimitive(v.toboolean());
		case LuaValue.TNIL:
			return JsonNull.INSTANCE;
		case LuaValue.TTHREAD:
			return new JsonPrimitive("thread(transient-value)");
		case LuaValue.TFUNCTION:
			return new JsonPrimitive("function(transient-value)");
		case LuaValue.TNUMBER:
			return new JsonPrimitive(v.todouble());
		case LuaValue.TTABLE:
			JsonObject ret = new JsonObject();
			LuaTable t = v.checktable();
			for(LuaValue k:t.keys()) {
				LuaValue v1 = t.get(k);
				ret.add(k.toString(), luaToJson(v1));
			}
			return ret;
		default:
			return new JsonPrimitive("userdata(transient-value)");
		}
	}

}
