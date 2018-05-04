package com.google.sites.clibonlineprogram.pokemonsms.net.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.luaj.vm2.LuaValue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PacketBuffer implements DataInput,DataOutput {
	private byte[] data;
	private int size;
	private int pos;
	private static final JsonParser parser = new JsonParser();
	public PacketBuffer() {
		data = new byte[16];
	}
	public PacketBuffer(byte[] data) {
		this.data = data;
		this.size = data.length;
	}

	private void resize(int req) {
		int newSize = data.length+req+10;
		byte[] newData = new byte[newSize];
		System.arraycopy(data, 0, newData, 0, data.length);
		data = newData;
	}

	@Override
	public void write(int arg0){
		if(size==data.length)
			resize(1);
		data[size++] = (byte)arg0;

	}
	@Override
	public void write(byte[] arg0) {
		write(arg0,0,arg0.length);
	}
	@Override
	public void write(byte[] b, int off, int len){
		if(size+len>=data.length)
			resize(len);
		System.arraycopy(b, off, data, size, len);
		size+= len;
	}
	@Override
	public void writeBoolean(boolean arg0){
		write(arg0?1:0);

	}
	@Override
	public void writeByte(int arg0){
		write(arg0);

	}
	@Override
	public void writeBytes(String arg0) {
		byte[] b = arg0.getBytes(StandardCharsets.UTF_8);
		byte[] write = new byte[2+b.length];
		write[0] = (byte)(arg0.length()>>8);
		write[1] = (byte)arg0.length();
		System.arraycopy(b, 0, write, 2, b.length);
	}
	@Override
	public void writeChar(int v){
		write(v>>8);
		write(v);
	}
	/**
	 * Writes chars. This does not corespond with the Packet System, do not use.
	 */
	@Override
	public void writeChars(String s){
		for(char c:s.toCharArray())
			writeChar(c);

	}
	/**
	 * Not a standard write, but implemented anyways.
	 */
	@Override
	public void writeDouble(double v){
		writeLong(Double.doubleToRawLongBits(v));

	}
	@Override
	public void writeFloat(float v) {
		writeInt(Float.floatToRawIntBits(v));

	}
	@Override
	public void writeInt(int v){
		write(v>>24);
		write(v>>16);
		write(v>>8);
		write(v);

	}
	@Override
	public void writeLong(long v){
		write((int) (v>>56));
		write((int) (v>>48));
		write((int) (v>>40));
		write((int) (v>>32));
		write((int) (v>>24));
		write((int) (v>>16));
		write((int) (v>>8));
		write((int) v);

	}

	@Override
	public void writeShort(int v){
		write(v>>8);
		write(v);

	}
	@Override
	public void writeUTF(String s){
		writeBytes(s);

	}

	public int read()  {

		return (int)data[pos++]&0xFF;

	}

	@Override
	public boolean readBoolean()  {
		// TODO Auto-generated method stub
		return read()==1;
	}
	@Override
	public byte readByte()  {
		// TODO Auto-generated method stub
		return (byte)read();
	}
	@Override
	public char readChar()  {
		// TODO Auto-generated method stub
		return (char)(read()<<8+read());
	}
	@Override
	public double readDouble()  {
		// TODO Auto-generated method stub
		return Double.longBitsToDouble(readLong());
	}
	@Override
	public float readFloat()  {
		// TODO Auto-generated method stub
		return Float.intBitsToFloat(readInt());
	}
	@Override
	public void readFully(byte[] arg0)  {
		readFully(arg0,0,arg0.length);

	}
	@Override
	public void readFully(byte[] b, int off, int len)  {
		System.arraycopy(data, pos, b, off, len);
		pos+=len;
	}
	@Override
	public int readInt()  {
		// TODO Auto-generated method stub
		return read()<<24+read()<<16+read()<<8+read();
	}
	@Override
	public String readLine()  {
		// TODO Auto-generated method stub
		return readUTF();
	}
	@Override
	public long readLong()  {
		// TODO Auto-generated method stub
		return readInt()<<32L+readInt();
	}
	@Override
	public short readShort()  {
		// TODO Auto-generated method stub
		return (short) (read()<<8+read());
	}
	@Override
	public String readUTF()  {
		int size = readUnsignedShort();
		byte[] b = new byte[size];
		readFully(b,0,size);
		String s = new String(b,StandardCharsets.UTF_8);
		return s;
	}
	@Override
	public int readUnsignedByte()  {
		// TODO Auto-generated method stub
		return read();
	}
	@Override
	public int readUnsignedShort()  {
		// TODO Auto-generated method stub
		return read()<<8+read();
	}
	@Override
	public int skipBytes(int arg0)  {
		pos+=arg0;
		return arg0;
	}

	public void writeUUID(UUID id) {
		writeLong(id.getMostSignificantBits());
		writeLong(id.getLeastSignificantBits());
	}

	public UUID readUUID()  {
		long high = readLong();
		long low = readLong();
		return new UUID(high,low);
	}

	public void writeVersion(String s) {
		String[] ver = s.split("\\.");
		String Ma = ver[0];
		String Mi = ver[1];
		int M = Integer.parseInt(Ma);
		int m = Integer.parseInt(Mi);
		write(M-1);
		write(m);
	}

	public String readVersion()  {
		int M = read()+1;
		int m = read();
		return M+"."+m;
	}

	public void writeJson(JsonObject o) {
		writeUTF(o.toString());
	}
	public JsonObject readJson() {
		return parser.parse(readUTF()).getAsJsonObject();
	}

	public byte[] toByteArray() {
		return data;
	}

	public void writeInstant(Instant time) {
		writeLong(time.getEpochSecond());
		writeInt(time.getNano());
	}
	public Instant readInstant() {
		long seconds = readLong();
		int nanos = readInt();
		return Instant.ofEpochSecond(seconds, nanos);
	}
	public void writeDuration(Duration dur) {
		writeLong(dur.getSeconds());
		writeInt(dur.getNano());
	}
	public Duration readDuration() {
		long seconds = readLong();
		int nanos = readInt();
		return Duration.ofSeconds(seconds, nanos);
	}

	public String readLongString() {
		byte[] buffer = new byte[readInt()];
		readFully(buffer);
		return new String(buffer,StandardCharsets.UTF_8);
	}
	public JsonObject readLongJson() {
		String data = readLongString();
		return parser.parse(data).getAsJsonObject();
	}
	public void writeLongString(String s) {
		byte[] buffer = s.getBytes(StandardCharsets.UTF_8);
		int size = buffer.length;
		writeInt(size);
		write(buffer);
	}
	public void writeLongJson(JsonObject o) {
		writeLongString(o.toString());
	}



}
