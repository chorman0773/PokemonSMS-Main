package com.google.sites.clibonlineprogram.pokemonsms.text;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.JsonObject;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent.*;

public class I18N extends I18NNode {
	private String language;
	private String dialect;
	private Locale wrappedLocale;
	private String name;


	public I18N(JsonObject o) {
		JsonObject info = o.getAsJsonObject("i18n_info");
		this.language = info.get("language").getAsString();
		this.dialect = info.get("dialect").getAsString();
		this.name = info.get("name").getAsString();
		wrappedLocale = new Locale(language, dialect);
		JsonObject entries = info.getAsJsonObject("entries");
		this.setTree(entries);
	}

	public TextComponent translateAndFormat(TranslatebleTextComponent comp,TextComponent[] args) {
		String target = comp.toUnformattedString().trim();
		String[] locations = target.split("\\.");
		I18NNode node = this;
		for(int i = 0;i<locations.length;i++)
			node = node.get(locations[i]);
		TextComponent ret = node.getComponent();
		return ret.format(args,new AtomicInteger());
	}

	public Locale getLocale() {
		return wrappedLocale;
	}
	public String getInternalName() {
		return language+"_"+dialect;
	}
	public String getName() {
		return name;
	}
	public TextComponent getNameComponent() {
		return new TextComponentString(name);
	}
}
