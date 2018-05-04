package com.google.sites.clibonlineprogram.pokemonsms.util;

import java.util.Map;

import org.luaj.vm2.LuaValue;

public class Preconditions {

	private Preconditions() {
		// TODO Auto-generated constructor stub
	}

	public static <T> T assertNonNull(T in) {
		if(in==null)
			throw new NullPointerException("Valued passed to assertNonNull is null");
		return in;
	}

	public static <T extends CharSequence> T assertNonEmpty(T seq) {
		assertNonNull(seq);
		if(seq.length()==0)
			throw new IllegalArgumentException("Passed CharSequence is empty");
		return seq;
	}

	public static <E,T extends Iterable<? extends E>> T assertNonEmpty(T iter){
		if(!iter.iterator().hasNext())
			throw new IllegalArgumentException("Passed Iterable is empty");
		return iter;
	}

	public static <K,V,T extends Map<? extends K,? extends V>> T assertNonEmpty(T map) {
		if(map.isEmpty())
			throw new IllegalArgumentException("Passed Map is empty");
		return map;
	}

	public static <U,T extends U> T assertType(U o, Class<T> clazz) {
		if(o==null)
			return null;
		if(!clazz.isInstance(o))
			throw new ClassCastException("Passed value is not of the type "+clazz.getTypeName());
		return (T)o;
	}


}
