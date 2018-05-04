package com.google.sites.clibonlineprogram.pokemonsms.lua;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import com.google.sites.clibonlineprogram.pokemonsms.util.Preconditions;

public class LuaJava {
	private static final Map<Class<?>,Function<Object,LuaValue>> coercions = new TreeMap<>((c1,c2)->c1.getTypeName().compareTo(c2.getTypeName()));


	private LuaJava() {
		// TODO Auto-generated constructor stub
	}
	public static LuaValue[] coerceAll(Object[] obj) {
		LuaValue[] v = new LuaValue[obj.length];
		for(int i=0;i<obj.length;i++)
			v[i] = coerce(obj[i]);

		return v;
	}
	public static LuaValue coerce(Object o) {
		Class<?> cl = o.getClass();
		while(cl!=Object.class&&!coercions.containsKey(cl))
			cl = cl.getSuperclass();
		if(coercions.containsKey(cl))
			return coercions.get(cl).apply(o);
		else
			return CoerceJavaToLua.coerce(o);
	}

	public static <T> T coerce(LuaValue v,Class<T> cl) {
		if(LuaValue.class.isAssignableFrom(cl))
			return Preconditions.assertType(v, cl);
		if(v.isnil())
			return null;
		if(v.isuserdata(cl))
			return Preconditions.assertType(v.checkuserdata(cl),cl);
		else
			{
			Object o = CoerceLuaToJava.coerce(v,cl);
			 return Preconditions.assertType(o,cl);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> void registerCoercion(Class<T> cl,Function<T,LuaValue> func) {
		coercions.put(cl, ((Function<Object,LuaValue>) (Function<?,LuaValue>)func));
	}

	public static <T> Iterable<T> toCoercedIterable(LuaTable t,Class<T> cl){
		final Iterator<T> itr = new CoercionIterator<>(t,cl);
		return new Iterable<>() {

			@Override
			public Iterator<T> iterator() {
				// TODO Auto-generated method stub
				return itr;
			}};
	}

	public static <T> Stream<T> toCoercedStream(LuaTable t,Class<T> cl){
		return StreamSupport.stream(toCoercedIterable(t,cl).spliterator(), false);
	}

}
