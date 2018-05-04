package com.google.sites.clibonlineprogram.pokemonsms.pokemon;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import com.google.sites.clibonlineprogram.pokemonsms.util.registry.ResourceLocation;
import com.google.sites.clibonlineprogram.sentry.save.RawSaveData.ListNode;
import com.google.sites.clibonlineprogram.sentry.save.Saveable;

public class Pokemon implements Saveable {
	private Species s;
	private Ability a;
	private int experience;
	private int level;
	private int growthRate;
	private int[] ivs = new int[6];
	private int[] evs = new int[6];
	private int[] stats = new int[6];
	private int hp;
	private int genderRatio;
	private int flags;
	private final UUID id;
	private String name;
	private String otName;
	private int otId;
	private int otSid;
	private int species;
	private int cycles;
	private int steps;
	private int form;
	private int natureId;
	private ResourceLocation caughtAt;
	private int caughtLevel;

	private static final Random rand = new Random();


	public static final int ATK = 0, DEF = 1, SPECIAL = 2,
			SPECDEF = 3, SPD = 4, HP = 5;
	public static final EnumNature[] nature = EnumNature.values();
	public enum EnumNature{
		HARDY(ATK,ATK),LONEY(ATK,DEF),ADAMANDT(ATK,SPECIAL),NAUGHTY(ATK,SPECDEF),BRAVE(ATK,SPD),
		BOLD(DEF,ATK),DOCILE(DEF,DEF),IMPISH(DEF,SPECIAL),LAX(DEF,SPECDEF),RELAXED(DEF,SPD),
		MODEST(SPECIAL,ATK),MILD(SPECIAL,DEF),BASHFUL(SPECIAL,SPECIAL),RASH(SPECIAL,SPECDEF),QUIET(SPECIAL,SPD),
		CALM(SPECDEF,ATK),GENTLE(SPECDEF,DEF),CAREFUL(SPECDEF,DEF),QUIRKY(SPECDEF,SPECDEF),SASSY(SPECDEF,SPD),
		TIMID(SPD,ATK),HASTY(SPD,DEF),JOLLY(SPD,SPECIAL),NAIVE(SPD,SPECDEF),SERIOUS(SPD,SPD);
		public final int increase;
		public final int decrease;
		public final String unName;
		EnumNature(int increase, int decrease) {
			this.increase = increase;
			this.decrease = decrease;
			this.unName = "natures."+name().toLowerCase(Locale.ROOT)+".name";
		}
		public boolean isNeutral() {
			return increase == decrease;
		}
	}

	public static final int FLAG_SHINY = 0x01,
			FLAG_SHINY_TYPE = 0x02,
			FLAG_SHINY_SUPER = 0x04,
			FLAG_FEMALE = 0x08,
			FLAG_HIDDEN = 0x10,
			FLAG_ABILITY_2 = 0x20;


	public Pokemon(UUID id) {
		this.id = id;
	}

	public void recalculateStats() {
		int[] base = s.getBaseStats(form);
		for(int i=0;i<stats.length;i++) {
			if(i==HP) {
				stats[i] = (int)((level*(2*base[i]+ivs[i]+(evs[i]/4))/100.0)+level+10);
			}else
				stats[i] = (int)((int)(level*(2*base[i]+ivs[i]+(evs[i]/4)/100.0)+5)*getNatureMod(natureId,i));
		}
	}

	private float getNatureMod(int nature, int i) {
		EnumNature n = Pokemon.nature[i];
		if(n.isNeutral())
			return 1.0f;
		else if(n.increase==i)
			return 1.1f;
		else if(n.decrease==i)
			return 0.9f;
		return 1.0f;
	}

	private static UUID generateUUID() {
		return new UUID(rand.nextLong(),rand.nextLong());
	}

	public static boolean willBeShiny(UUID id,int otId, int otSid) {
		int a = (int)(id.getMostSignificantBits()>>44)&0xF;
		int b = (int)(id.getMostSignificantBits()>>28)&0xF;
		int c = (int)(id.getMostSignificantBits()>>20)&0xF;
		int d = (int)(id.getLeastSignificantBits()>>44)&0xF;
		int ab = a<<8+b;
		int cd = a<<8+d;
		int i = (otId^otSid)&0xFFFF;
		int v = i^(ab<<16+cd);
		return v<16;
	}

	public static final Pokemon generatePokemon(Species s,boolean useHidden, int shinyTries, int perfectIvs, int maxEvs,int otId, int otSid,String otName) {
		UUID id = generateUUID();
		if(!willBeShiny(id,otId,otSid)&&s.canBeShiny())
			for(int i = 0;i<shinyTries&&!willBeShiny(id,otId,otSid);i++,id=generateUUID());
		Pokemon ret = new Pokemon(id);
		ret.otName = otName;
		ret.otId = otId;
		ret.otSid = otSid;
		long high = id.getMostSignificantBits();
		long low = id.getLeastSignificantBits();
		int iv = (int)(low&0x7FFFFFFF);
		int[] ivs = new int[] {iv&0x1F,(iv>>=2)&0x1F,(iv>>=2)&0x1f,(iv>>=2)&0x1f,(iv>>=2)&0x1f,(iv>>=2)&0x1f};
		for(int i =0;i<perfectIvs&&!areAllIvsPerfect(ivs);i++) {
			int j = rand.nextInt(6);
			while(ivs[j]==31)
				j = rand.nextInt(6);
			ivs[j] = 31;
		}

		return ret;
	}

	private static boolean areAllIvsPerfect(int[] ivs) {
		for(int i:ivs)
			if(i!=31)
				return false;

	return true;
	}


	@Override
	public void save(ListNode l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load(ListNode l) {
		// TODO Auto-generated method stub

	}

}
