package com.google.sites.clibonlineprogram.pokemonsms.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

import com.google.sites.clibonlineprogram.pokemonsms.util.KeyBinds;
import com.google.sites.clibonlineprogram.pokemonsms.util.Preconditions;
import com.google.sites.clibonlineprogram.pokemonsms.util.RegexConstants;
import com.google.sites.clibonlineprogram.sentry.GameBasic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.sites.clibonlineprogram.pokemonsms.PokemonSmsGame;
import static com.google.sites.clibonlineprogram.pokemonsms.util.Preconditions.*;

/**
 *
 * @author Connor Horman
 */

public enum Options implements RegexConstants {
	INSTANCE;
	public interface ValueType{
		public boolean validate(JsonElement value);
		public Object getValue(JsonElement value)throws Exception,IllegalArgumentException;
		public JsonElement storeValue(Object value)throws IllegalArgumentException;
	}

	public static class EnumValue<E extends Enum<E>> implements ValueType{
		private final Class<E> enumCl;
		public EnumValue(Class<E> enumCl) {
			this.enumCl = enumCl;
		}
		@Override
		public boolean validate(JsonElement value) {
			try {
			Enum.valueOf(enumCl, value.getAsString().toUpperCase(Locale.ROOT));
			return true;
			}catch(IllegalArgumentException e) {
				return false;
			}
		}

		@Override
		public E getValue(JsonElement value) {
			if(!validate(value))
				return null;
			return Enum.valueOf(enumCl, value.getAsString().toUpperCase(Locale.ROOT));
		}
		public JsonElement storeValue(Object o) {
			E val = Preconditions.assertType(o,enumCl);
			return new JsonPrimitive(val.name());
		}

	}

	public enum Primitive implements ValueType{
		STRING(".*"),
		UUID(uuid),
		CLASS(identifierQualified),
		INTEGER("[-+]?[0-9]+"),
		DOUBLE("[-+]?[0-9]+(\\.[0-9]+)([eE][0-9]+)"),
		BOOLEAN("true|false");
		private final String pattern;
		private Primitive(String pattern){
			this.pattern = pattern;
			Pattern.compile(pattern);
		}
		public boolean validate(JsonElement value) {
			if(!value.isJsonPrimitive())
				return false;
			else {
				JsonPrimitive primVal = value.getAsJsonPrimitive();
				switch(this) {
					case STRING:
					case UUID:
					case CLASS:
						return primVal.isString()&&primVal.getAsString().matches(pattern);
					case INTEGER:
						try {
						return primVal.isNumber()&&primVal.getAsInt()==primVal.getAsDouble();
						}catch(NumberFormatException e) {
							return false;
						}
					case DOUBLE:
						return primVal.isNumber();
					case BOOLEAN:
						return primVal.isBoolean();
				}
				return false;
			}
		}
		public Object getValue(JsonElement value) throws ClassNotFoundException,IllegalArgumentException {
			if(!validate(value))
				throw new IllegalArgumentException(String.format("%s does not match %s.", value,pattern));
			JsonPrimitive primVal = value.getAsJsonPrimitive();
			switch(this) {
			case STRING:
				return primVal.getAsString();
			case UUID:
				return java.util.UUID.fromString(primVal.getAsString());
			case CLASS:
				return Class.forName(primVal.getAsString());
			case INTEGER:
				return primVal.getAsInt();
			case DOUBLE:
				return primVal.getAsDouble();
			case BOOLEAN:
				return primVal.getAsBoolean();
			}
			throw new IllegalStateException("WTF");
		}
		public JsonElement storeValue(Object o) {
			switch(this) {
				case UUID:
					java.util.UUID uuid = Preconditions.assertType(o, java.util.UUID.class);
					return new JsonPrimitive(uuid.toString());
				case CLASS:
					Class<?> cl = Preconditions.assertType(o, Class.class);
					return new JsonPrimitive(cl.getName());
				case STRING:
					String s = Preconditions.assertType(o, String.class);
					return new JsonPrimitive(s);
				case INTEGER:
					int i = Preconditions.assertType(o, int.class);
					return new JsonPrimitive(i);
				case DOUBLE:
					double d = Preconditions.assertType(o, double.class);
					return new JsonPrimitive(d);
			}
			throw new IllegalStateException("WutFace");
		}
	}

	private final File file;
	private boolean dirty;
	private JsonObject options;
	public interface Option{
		ValueType getType();
		String getKey();
		JsonElement getDefault();
		void onUpdate();
	}



	public enum OptionStandard implements Option{
		DEFOCUS_MODE(Primitive.BOOLEAN,"game.options.defocus",new JsonPrimitive(false));
		private final ValueType type;
		private final String key;
		private final JsonElement def;

		OptionStandard(ValueType type,String key,JsonElement def){
			this.type = type;
			this.key = key;
			this.def = def;
		}
		@Override
		public ValueType getType() {
			// TODO Auto-generated method stub
			return type;
		}

		@Override
		public String getKey() {
			// TODO Auto-generated method stub
			return key;
		}

		@Override
		public JsonElement getDefault() {
			// TODO Auto-generated method stub
			return def;
		}
		@Override
		public void onUpdate() {
			// TODO Auto-generated method stub

		}

	}
	public static enum Controls implements Option{
		MOVE_UP("movement.up",'w');
		private final int defbind;
		private final String name;
		Controls(String name,int keybind) {
			this.name = "controls."+name;
			this.defbind = keybind;
		}
		@Override
		public ValueType getType() {
			// TODO Auto-generated method stub
			return Primitive.INTEGER;
		}
		@Override
		public String getKey() {
			// TODO Auto-generated method stub
			return name;
		}
		@Override
		public JsonElement getDefault() {
			// TODO Auto-generated method stub
			return new JsonPrimitive(defbind);
		}
		@Override
		public void onUpdate() {
			KeyBinds.updateKeyBind(this);

		}
	}


	private Options() {
		try {
		file = new File(GameBasic.instance.getDirectory(),"options.json");
		if(!file.exists()) {
			file.createNewFile();
			options = new JsonObject();
		}else
			options = PokemonSmsGame.json.parse(new FileReader(file)).getAsJsonObject();

		}catch(IOException e) {
			throw new RuntimeException("Error loading Options",e);
		}
	}

	private JsonElement getOptionKey(String name) {
		String[] groups = name.split("\\.");
		JsonObject o = options;
		for(int i = 0;i<groups.length-1;i++)
			o = o.getAsJsonObject(groups[i]);
		JsonElement e = o.get(groups[groups.length-1]);
		return e;
	}
	private void setOptionKey(String name,JsonElement e)
	{
		String[] groups = name.split("\\.");
		JsonObject o = options;
		for(int i = 0;i<groups.length-1;i++)
			o = o.getAsJsonObject(groups[i]);
		name = groups[groups.length-1];
		if(o.has(name))
			o.remove(name);
		o.add(name, e);
	}



	@SuppressWarnings("unchecked")
	public <T> T getOption(Option o,Class<T> target) throws IllegalArgumentException, Exception {
		JsonElement ret = getOptionKey(o.getKey());//TODO
		ValueType v = o.getType();
		return assertType(v.getValue(ret),target);
	}

	public int getIntOption(Option o) throws IllegalArgumentException, Exception {
		if(o.getType()!=Primitive.INTEGER)
			throw new IllegalArgumentException("Only an integer type can be used");
		return getOption(o,int.class);
	}

	public double getDoubleOption(Option o)throws IllegalArgumentException, Exception {
		if(o.getType()!=Primitive.DOUBLE)
			throw new IllegalArgumentException("Only a double type can be used");
		return getOption(o,double.class);
	}

	public boolean getBooleanOption(Option o)throws IllegalArgumentException, Exception{
		if(o.getType()!=Primitive.BOOLEAN)
			throw new IllegalArgumentException("Only a boolean type can be used");
		return this.getOption(o,boolean.class);
	}

	public <E extends Enum<E>> E getEnumOption(Option o)throws IllegalArgumentException, Exception{
		if(!(o.getType() instanceof EnumValue))
			throw new IllegalArgumentException("Only an enum value can be used");
		return getOption(o,((EnumValue<E>)o.getType()).enumCl);
	}

	public <T> void setOption(Option o,T value) {
		ValueType t = o.getType();
		setOptionKey(o.getKey(),t.storeValue(value));
		o.onUpdate();
		dirty = true;
	}

	public void save() throws FileNotFoundException, IOException {
		if(!dirty)
			return;
		dirty = false;
		PokemonSmsGame.gson.toJson(options, new FileWriter(file));
	}

}
