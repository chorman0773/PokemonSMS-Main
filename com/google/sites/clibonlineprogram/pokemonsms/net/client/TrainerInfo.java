package com.google.sites.clibonlineprogram.pokemonsms.net.client;

import java.awt.image.BufferedImage;

import com.google.gson.JsonObject;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;
import com.google.sites.clibonlineprogram.pokemonsms.util.Base64Images;

public class TrainerInfo {
	private final BufferedImage profile;
	private final TextComponent name;
	public TrainerInfo(TextComponent name,BufferedImage profile) {
		this.profile = profile;
		this.name = name;
	}
	public TrainerInfo(JsonObject o) {
		String stored = o.get("Image").getAsString();
		name = TextComponent.fromJson(o.getAsJsonObject("Name"));
		profile = Base64Images.b642img(stored);
	}

	public JsonObject toObject() {
		JsonObject o = new JsonObject();
		o.add("Name", name.toJson());
		o.addProperty("Image", Base64Images.img2b64(profile));
		return o;
	}

}
