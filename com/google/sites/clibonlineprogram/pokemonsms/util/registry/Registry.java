package com.google.sites.clibonlineprogram.pokemonsms.util.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import com.google.sites.clibonlineprogram.pokemonsms.lua.LuaJava;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.item.Item;

public class Registry<T> {
	private Map<ResourceLocation,T> registry = new TreeMap<>();
	private List<T> numberRegistries = new ArrayList<>();
	private T def;
	public final LuaValue luaV = new Delegate();

	public class Delegate extends LuaUserdata{

		public Delegate() {
			super(null);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#checkuserdata()
		 */
		@Override
		public Registry<T> checkuserdata() {
			// TODO Auto-generated method stub
			return Registry.this;
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#checkuserdata(java.lang.Class)
		 */
		@Override
		public Registry<T> checkuserdata(Class arg0) {
			if(!arg0.isInstance(Registry.this))
				throw new ClassCastException("This is only ever a Registry");
			return checkuserdata();
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#isuserdata()
		 */
		@Override
		public boolean isuserdata() {
			// TODO Auto-generated method stub
			return true;
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#isuserdata(java.lang.Class)
		 */
		@Override
		public boolean isuserdata(Class arg0) {
			// TODO Auto-generated method stub
			return arg0.isInstance(Registry.this);
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#optuserdata(java.lang.Class, java.lang.Object)
		 */
		@Override
		public Registry<T> optuserdata(Class arg0, Object arg1) {
			// TODO Auto-generated method stub
			return checkuserdata(arg0);
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#optuserdata(java.lang.Object)
		 */
		@Override
		public Registry<T> optuserdata(Object arg0) {
			// TODO Auto-generated method stub
			return checkuserdata();
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#touserdata()
		 */
		@Override
		public Object touserdata() {
			// TODO Auto-generated method stub
			return checkuserdata();
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#touserdata(java.lang.Class)
		 */
		@Override
		public Object touserdata(Class arg0) {
			// TODO Auto-generated method stub
			return checkuserdata(arg0);
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaValue#invokemethod(java.lang.String, org.luaj.vm2.LuaValue[])
		 */
		@Override
		public Varargs invokemethod(String arg0, LuaValue[] arg1) {
			// TODO Auto-generated method stub
			return super.invokemethod(arg0, arg1);
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaValue#invokemethod(java.lang.String, org.luaj.vm2.Varargs)
		 */
		@Override
		public Varargs invokemethod(String arg0, Varargs arg1) {
			// TODO Auto-generated method stub
			return super.invokemethod(arg0, arg1);
		}

	}
	static {
		LuaJava.registerCoercion(Registry.class, r->r.luaV);
	}

	public Registry() {

	}
	public void register(ResourceLocation key,T value) {
		registry.put(key, value);
		numberRegistries.add(value);
	}
	public T getById(int id) {
		if(numberRegistries.size()<=id)
			return def;
		else
			return numberRegistries.get(id);
	}
	public T getByName(ResourceLocation p) {
		if(!registry.containsKey(p))
			return def;
		else
			return registry.get(p);
	}
	public void setDefault(T def) {
		this.def = def;
	}
	public T getByName(String checkjstring) {
		// TODO Auto-generated method stub
		return getByName(new ResourceLocation(checkjstring));
	}


}
