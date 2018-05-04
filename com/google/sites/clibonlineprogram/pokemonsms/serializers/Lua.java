package com.google.sites.clibonlineprogram.pokemonsms.serializers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaNil;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;


public class Lua {

	private Lua() {
		// TODO Auto-generated constructor stub
	}
	private static final byte[] serializeNumber(LuaNumber n) {
			double d = n.checkdouble();
			long l = Double.doubleToRawLongBits(d);
			byte[] ret = new byte[8];
			ret[0] = (byte)((l>>56)&0xFF);
			ret[1] = (byte)((l>>48)&0xFF);
			ret[2] = (byte)((l>>40)&0xFF);
			ret[3] = (byte)((l>>32)&0xFF);
			ret[4] = (byte)((l>>24)&0xFF);
			ret[5] = (byte)((l>>16)&0xFF);
			ret[6] = (byte)((l>>8)&0xFF);
			ret[7] = (byte)(l&0xFF);
			return ret;
	}
	private static final byte[] serializeString(LuaString s) {
		return s.m_bytes;
	}
	private static final byte[] serializeType(LuaValue v) {
		byte[] b = new byte[1];
		b[0]= (byte) v.type();
		return b;
	}
	private static final byte[] serializeTable(LuaTable t) {
		t = new LuaTable(t);//Copy t.
		final LuaTable b = t;
		int len = t.length();

		ByteArrayOutputStream array = new ByteArrayOutputStream();
		try(DataOutputStream str = new DataOutputStream(array)){
			str.writeInt(len);
			for(int i = 0;i<len;i++) {
				LuaValue v = t.get(i);
				str.write(serializeLua(v));
			}
			Stream<LuaValue> s = Arrays.stream(t.keys()).filter(new Predicate<LuaValue>() {

				@Override
				public boolean test(LuaValue arg0) {
					// TODO Auto-generated method stub
					return !((arg0.isint()&&arg0.checkint()>0)||(arg0.type()==LuaValue.TFUNCTION||b.get(arg0).type()==LuaValue.TFUNCTION));
				}
			});
			int size = (int) s.count();
			LuaValue[] keys = s.toArray(i->new LuaValue[i]);
			str.writeInt(size);
			for(LuaValue k:keys) {
				str.write(serializeLua(k));
				str.write(serializeLua(t.get(k)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return array.toByteArray();
	}

	public static byte[] serializeFunction(LuaFunction f) {
		if(f.isclosure()) {
		LuaClosure c = f.checkclosure();
		if(c.upValues.length!=0)
			return new byte[] {0,0,0,0};//Can't serialize this
		Prototype call = c.p;
		int[] is = call.code;
		byte[] b = new byte[4*(1+is.length)];

		int i = is.length;
		b[0] = (byte)i;
		b[1] = (byte)(i>>8);
		b[2] = (byte)(i>>16);
		b[3] = (byte)(i>>24);

		for(int j = 0;j<i;j++) {
			b[4+4*j] = (byte)is[j];
			b[5+4*j] = (byte)(is[j]>>8);
			b[6+4*j] = (byte)(is[j]>>16);
			b[7+4*j] = (byte)(is[j]>>24);
		}
			return b;
		}else {
			//not much I can do, so return a length of 0.
			return new byte[] {0,0,0,0};
		}
	}

	private static byte[] serializeLua(LuaValue v) {
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		if(v.type()!=LuaValue.TFUNCTION)
			array.write(serializeType(v), 0, 1);
		else
			return serializeLua(LuaValue.NIL);
		byte[] tbl;
		switch(v.type()) {
		case LuaValue.TNUMBER:
		case LuaValue.TINT:
			tbl = serializeNumber(v.checknumber());
		break;
		case LuaValue.TBOOLEAN:
			array.write(v.toboolean()?1:0);
		break;
		case LuaValue.TTABLE:
			array.write(tbl = serializeTable(v.checktable()),0,tbl.length);
		break;
		case LuaValue.TFUNCTION:
			array.write(tbl = serializeFunction(v.checkfunction()),0,tbl.length);
		break;
		default:

		break;
		}
		return array.toByteArray();
	}




	public static void writeToStream(LuaValue v,OutputStream s)throws IOException{
		s.write(serializeLua(v));
	}

}
