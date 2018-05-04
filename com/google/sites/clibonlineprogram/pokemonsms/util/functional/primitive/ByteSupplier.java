package com.google.sites.clibonlineprogram.pokemonsms.util.functional.primitive;
/**
 * Represents a Supplier of bytes
 * @author Connor Horman
 *
 */
@FunctionalInterface
public interface ByteSupplier {
	byte get();
	default int getUnsigned() {
		return get()&0xFF;
	}
	default short getShort() {
		return (short) (getUnsigned()|getUnsigned()<<8);
	}
	default int getInt() {
		return (getUnsigned()|getUnsigned()<<8|getUnsigned()<<16|getUnsigned()<<24);
	}
	default long getLong() {
		return getInt()|getInt()<<32L;
	}
	default int getUnsignedShort() {
		return getUnsigned()|getUnsigned()<<8;
	}
	default float getFloat() {
		return Float.intBitsToFloat(getInt());
	}
	default double getDouble() {
		return Double.longBitsToDouble(getLong());
	}
}
