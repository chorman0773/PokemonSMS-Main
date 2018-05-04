package com.google.sites.clibonlineprogram.pokemonsms.util;

public class Versions {

	private Versions() {
		// TODO Auto-generated constructor stub
	}



	public static int getMajor(int version) {
		return ((version>>8)&0xff)+1;
	}
	public static int getMinor(int version) {
		return version&0xff;
	}
	public static int ofPair(int major,int minor) {
		return ((major-1)&0xff)<<8+minor&0xff;
	}

	public static int fromString(String version) {
		String[] vals = version.split("\\.");
		return ofPair(Integer.parseInt(vals[0]),Integer.parseInt(vals[1]));
	}
	public static String toString(int encoded) {
		return getMajor(encoded)+"."+getMinor(encoded);
	}
	public static class Numbers{
		public static class Current{
			public static final int GAME = fromString("1.0");
			public static final int NET = fromString("1.0");
			public static final int SAVE = fromString("1.0");
		}
		public static class Minimum{
			public static final int GAME = fromString("1.0");
			public static final int NET = fromString("1.0");
			public static final int SAVE = fromString("1.0");
		}
	}
	public static class Strings{
		public static class Current{
			public static final String GAME = "1.0";
			public static final String NET = "1.0";
			public static final String SAVE = "1.0";
		}
		public static class Minimum{
			public static final String GAME = "1.0";
			public static final String NET = "1.0";
			public static final String SAVE = "1.0";
		};
	};
}
