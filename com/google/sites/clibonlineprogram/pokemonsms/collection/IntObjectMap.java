package com.google.sites.clibonlineprogram.pokemonsms.collection;

import java.util.Set;

public interface IntObjectMap<V> {
	public static interface Entry<V>{
		int getKey();
		V getValue();
		void setValue(V val);
	}
	public V put(int k,V v);
	public V get(int k);
	public boolean hasKey(int k);
	public boolean hasValue(V val);
	public Set<Entry<V>> entrySet();
	public IntSet keySet();
	public Iterable<V> values();
}
