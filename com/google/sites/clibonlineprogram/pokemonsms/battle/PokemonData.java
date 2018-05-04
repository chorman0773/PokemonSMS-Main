package com.google.sites.clibonlineprogram.pokemonsms.battle;

import com.google.sites.clibonlineprogram.pokemonsms.net.packet.PacketBuffer;
import com.google.sites.clibonlineprogram.pokemonsms.pokemon.Pokemon;
import com.google.sites.clibonlineprogram.pokemonsms.text.TextComponent;
import com.google.sites.clibonlineprogram.pokemonsms.util.registry.ResourceLocation;

public class PokemonData {
	private int id;
	private ResourceLocation pokemonLoc;
	private int pallate;
	private int form;
	private float hp;
	private EnumStatusCondition status;
	private TextComponent name;
	private float cryVolume;
	private float cryPitch;
	private int bytes;
	private int level;
	private boolean badPoison;

	public PokemonData(int id,Pokemon pkm) {
		this.id = id;

	}
	public PokemonData(int id,PacketBuffer buff) {
		this.id = id;
		this.pokemonLoc = new ResourceLocation(buff.readUTF());
		this.pallate = buff.readUnsignedByte();
		this.form = buff.readUnsignedShort();
		this.hp = buff.readFloat();
		int status = buff.readUnsignedByte();

		if(status==2)
			this.badPoison=true;

		this.status = EnumStatusCondition.ofNetworkEnum(status);
		this.name = TextComponent.fromJson(buff.readJson());
		this.cryVolume = buff.readUnsignedByte()/128f;
		this.cryPitch = buff.readUnsignedByte()/128f;
		this.bytes = buff.readInt();
		this.level = buff.readUnsignedShort();
	}

	public void writeToBuffer(PacketBuffer buff) {
		if(this.id>=0)
			buff.writeByte(id);
		buff.writeBytes(pokemonLoc.toString());
		buff.writeByte(pallate);
		buff.writeShort(form);
		buff.writeFloat(hp);
		if(this.badPoison&&this.status==EnumStatusCondition.POISON)
			buff.writeByte(2);
		else if(this.status==EnumStatusCondition.POISON)
			buff.writeByte(1);
		else if(this.status==EnumStatusCondition.OK)
			buff.writeByte(0);
		else
			buff.writeByte(status.ordinal()-1);
		buff.writeJson(this.name.toJson());
		buff.writeByte((int) this.cryVolume*128);
		buff.writeByte((int)this.cryPitch*128);
		buff.writeInt(bytes);
		buff.writeShort(level);
	}
}
