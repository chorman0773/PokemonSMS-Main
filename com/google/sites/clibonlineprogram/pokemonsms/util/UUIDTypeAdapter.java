package com.google.sites.clibonlineprogram.pokemonsms.util;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class UUIDTypeAdapter implements JsonDeserializer<UUID>, JsonSerializer<UUID> {
	public static final UUID NIL = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public UUIDTypeAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

	@Override
	public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if(!UUID.class.equals(typeOfT))
			throw new ClassCastException("This producer cannot generate UUIDs of a type other than java.util.UUID (Ljava/util/UUID;)");
		String uuid = json.getAsString();
		try {
			return UUID.fromString(uuid);
		}catch(IllegalArgumentException e) {
			throw new JsonParseException("UUID's must take the form [0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}");
		}
	}

}
