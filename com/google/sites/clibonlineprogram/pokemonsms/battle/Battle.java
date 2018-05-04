package com.google.sites.clibonlineprogram.pokemonsms.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.google.sites.clibonlineprogram.pokemonsms.battle.condition.EnumWeatherType;
import com.google.sites.clibonlineprogram.pokemonsms.battle.condition.Weather;
import com.google.sites.clibonlineprogram.pokemonsms.events.EventBus;
import com.google.sites.clibonlineprogram.pokemonsms.linker.LuaContext;
import com.google.sites.clibonlineprogram.pokemonsms.lua.LuaJava;
import com.google.sites.clibonlineprogram.pokemonsms.side.EnumSide;
import com.google.sites.clibonlineprogram.pokemonsms.util.registry.ResourceLocation;

import static com.google.sites.clibonlineprogram.pokemonsms.battle.combat.Targeting.*;

public abstract class Battle {
	protected final LuaValue battleT;
	private Weather curr;
	private Side[] sides;
	private List<ActivePokemon> activePokemon;
	private Map<ActivePokemon,Side> sideOfPokemon;
	private LuaValue eventBus;
	private int[][] layout;
	private int[][] invLayout;
	private BattleRules rules;


	public class BattleLuaDelegate extends LuaUserdata{

		public BattleLuaDelegate() {
			super(Battle.this);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#set(org.luaj.vm2.LuaValue, org.luaj.vm2.LuaValue)
		 */
		@Override
		public void set(LuaValue arg0, LuaValue arg1) {
			// TODO Auto-generated method stub
			error("Can't set values");
		}

		/* (non-Javadoc)
		 * @see org.luaj.vm2.LuaUserdata#touserdata(java.lang.Class)
		 */
		@Override
		public Battle touserdata(Class arg0) {
			if(!Battle.this.getClass().isAssignableFrom(arg0))
				throw new ClassCastException("Can't typeshift this value");
			return Battle.this;
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
		LuaJava.registerCoercion(Battle.class, b->b.battleT);
	}

	public Battle(BattleRules rules) {
		battleT = new BattleLuaDelegate();
		this.rules = rules;
		this.sides = new Side[2];
		this.layout = new int[2][rules.getMode().getPokemonPerSide()];
		this.invLayout = new int[2*rules.getMode().getPokemonPerSide()][2];
	}

	public Weather startWeather(EnumWeatherType t,int timer) {

		eventBus.invokemethod("fire", new LuaValue[] {LuaContext.require("Events").get("Battle").get("Weather").get("Start"),CoerceJavaToLua.coerce(t),CoerceJavaToLua.coerce(timer),battleT});
		Weather w = new Weather(t,false,timer);
		eventBus.invokemethod("fire", new LuaValue[]{LuaContext.require("Events").get("Battle").get("Weather").get("Timer"),CoerceJavaToLua.coerce(w),battleT});
		this.curr = w;
		return w;
	}
	public Weather startExtremeWeather(EnumWeatherType t) {
		eventBus.invokemethod("fire", new LuaValue[] {LuaContext.require("Events").get("Battle").get("Weather").get("StartExtreme"),LuaJava.coerce(t),battleT});
		Weather w = new Weather(t,true,1);
		this.curr = w;
		return w;
	}
	public Weather getWeather() {
		return this.curr;
	}

	public int getPkmId(ActivePokemon pkm) {
		return activePokemon.indexOf(pkm);
	}

	public boolean isEventBattle() {
		return eventBus!=null&&!eventBus.isnil();
	}

	public boolean isServer() {
		return EnumSide.getSide()==EnumSide.SERVER;
	}

	public abstract boolean isOwnedBy(ActivePokemon pkm) ;

	public ActivePokemon[] select(ActivePokemon src,int targeting) {
		List<ActivePokemon> values = new ArrayList<>();
		if((targeting&SELF)!=0)
			values.add(src);
		boolean adjacentOnly = false;
		int loc = getPkmId(src);
		int[] target = invLayout[loc];
		if((targeting&ADJACENT)!=0)
			{
			if((targeting&ENEMY)!=0||(targeting&ADJACENT)==targeting){
				int[] ntarget = {target[0]==0?1:0,target[1]};
				values.add(activePokemon.get(layout[ntarget[0]][ntarget[1]]));
				if(ntarget[1]<(layout[ntarget[0]].length-1))
					values.add(activePokemon.get(layout[ntarget[0]][ntarget[1]+1]));
				if(ntarget[1]>0)
					values.add(activePokemon.get(layout[ntarget[0]][ntarget[1]-1]));
				}
			if((targeting&ALLY)!=0||(targeting&ADJACENT)==targeting) {
				if(target[1]<(layout[target[0]].length-1))
					values.add(activePokemon.get(layout[target[0]][target[1]+1]));
				if(target[1]>0)
					values.add(activePokemon.get(layout[target[0]][target[1]-1]));
				}
			}
		else {
			if((targeting&ENEMY)!=0)
				for(int pos:layout[target[0]==0?1:0])
					values.add(activePokemon.get(pos));
			if((targeting&ALLY)!=0)
				for(int pos:layout[target[0]])
					if(loc!=pos)
						values.add(activePokemon.get(pos));
			if((targeting&ALL)!=0&&(targeting&(ALL|SELF))==targeting)
				for(int[] hpos:layout)
					for(int pos:hpos)
						if(pos!=loc)
							values.add(activePokemon.get(pos));
		}


		return values.stream().toArray(ActivePokemon[]::new);
	}

	public void dealExplosiveDamage(ActivePokemon[] targets, int power,ActivePokemon src) {

	}






}
