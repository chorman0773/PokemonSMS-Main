package com.google.sites.clibonlineprogram.pokemonsms.serializers;

import com.google.gson.JsonObject;

public interface JsonSerializeable {
	public void serialize(JsonObject o);
	public void deserialize(JsonObject o);
}
