package com.google.sites.clibonlineprogram.pokemonsms.text;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.sites.clibonlineprogram.pokemonsms.util.Preconditions;


public class I18NNode {
	public static class I18NNodeAdapter implements  JsonDeserializer<I18NNode>{

		@Override
		public I18NNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			return new I18NNode(json.getAsJsonObject());
		}


	}

	private TextComponent comp;
	private Map<String,I18NNode> tree;
	public I18NNode(JsonObject base) {
		if(!base.has("component")&&!base.has("tree"))
			throw new IllegalArgumentException("Supplied Node must either define text or a tree");
		if(base.has("component"))
			comp = TextComponent.gson.fromJson(base.get("component"), TextComponent.class);
		else
			comp = null;
		if(base.has("tree"))
			setTree(base.getAsJsonObject("tree"));
		else
			tree = Collections.emptyMap();

	}
	protected I18NNode() {}

	protected final void setTree(JsonObject o) {
		tree = new TreeMap<>(o.entrySet().stream().collect(Collectors.toMap(new Function<Map.Entry<String,JsonElement>,String>(){

			@Override
			public String apply(Entry<String, JsonElement> t) {
				// TODO Auto-generated method stub
				return t.getKey();
			}

		},new Function<Map.Entry<String,JsonElement>, I18NNode>(){

			@Override
			public I18NNode apply(Entry<String, JsonElement> t) {
				// TODO Auto-generated method stub
				return TextComponent.gson.fromJson(t.getKey(), I18NNode.class);
			}})));
	}
	public I18NNode get(String localKey) {
		if(tree.isEmpty())
			throw new IllegalStateException("This node does not have an associated tree");
		return tree.get(localKey);
	}

	public TextComponent getComponent() {
		return Preconditions.assertNonNull(comp);
	}
}
