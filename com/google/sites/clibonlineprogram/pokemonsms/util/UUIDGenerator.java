package com.google.sites.clibonlineprogram.pokemonsms.util;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * A Generator for Version 1, Varient 2 UUIDs.<br/>
 * The UUIDs generated have a Random MAC Address as to not expose information about the computer that generated it.<br/>
 * As per the specification, the resultant UUID sets the MSB of its Mac Address to 1.<br/>
 * @author Connor Horman
 *
 */
public class UUIDGenerator {

	private static final SecureRandom macRandom = new SecureRandom();
	private static final long VERSION_MASK =   0x0000000000008000L;
	private static final long CLOCK_LOW_MASK = 0xFFFFFFFF00000000L;
	private static final long CLOCK_MID_MASK = 0x00000000FFFF0000L;
	private static final long CLOCK_HI_MASK =  0x0000000000007FFFL;

	private static final long VARIENT_MASK =   0xC000000000000000L;
	private static final long CLOCK_SEQ_MASK = 0x3FFF000000000000L;
	private static final long MAC_MASK =       0x00007FFFFFFFFFFFL;
	private static final long MAC_RAND_APPEND =0x0000800000000000L;

	private static final long VERSION =        0x000000000001000L;
	private static final long VARIENT =        0x200000000000000L;

	private static final Instant UUID_EPOCH = Instant.parse("1582-10-15T00:00:00.00Z");

	private UUIDGenerator() {

	}

	public static final UUID generate() {
		return generate(Instant.now());
	}
	public static final UUID generate(Instant stamp) {
		Duration d = Duration.between(UUID_EPOCH, stamp);
		long ts = d.getSeconds()*10000000 + d.getNano()/100;
		long clockSeq = (Long.hashCode(d.getNano()%100)&0x3FFF)<<48L;
		long low = (ts&0xFFFFFFFFL)<<32;
		long mid = ((ts>>32)&0xFFFF)<<16;
		long hi = ((ts>>48)&0x7FFF)<<48;
		long mac = macRandom.nextLong()&MAC_MASK;
		long uuidMost = (low&CLOCK_LOW_MASK) | (mid&CLOCK_MID_MASK) | (VERSION&VERSION_MASK)| (hi&CLOCK_HI_MASK);
		long uuidLeast = (VARIENT&VARIENT_MASK) | (clockSeq&CLOCK_SEQ_MASK) | MAC_RAND_APPEND | (mac&MAC_MASK);
		return new UUID(uuidMost,uuidLeast);
	}


}
