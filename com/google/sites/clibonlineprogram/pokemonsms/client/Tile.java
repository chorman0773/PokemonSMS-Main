package com.google.sites.clibonlineprogram.pokemonsms.client;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class Tile {

	private int idx;
	private LuaValue base;
	private int flags;
	private String graphicsPath;
	private boolean enabledTileSpecificGraphics;
	private boolean enabledCollision;

	public static final int USE_BACKGROUND = 0x01, HAS_COLLISION = 0x02, OVERRIDE_TILESET = 0x04,
			HAS_SEMICOLISSION = 0x08;

	public Tile(int idx,LuaValue base) {
		this.idx = idx;
		this.base = base;
		LuaValue flags = base.get("flags");
		this.flags = flags.checkint();
		this.graphicsPath = base.get("image").checkjstring();
		this.enabledTileSpecificGraphics = !base.get("getGraphics").isnil();
		this.enabledCollision = !base.get("checkCollision").isnil();
	}

	public String getGraphicsPath(String tileset) {
		if((this.flags&OVERRIDE_TILESET) != 0)
			return "tiles.base."+graphicsPath;
		return "tiles."+tileset+"."+graphicsPath;
	}

	public String getGraphicsPath(String tileset,LuaTable instance) {
		if(this.enabledTileSpecificGraphics) {
			LuaValue out = base.get("getGraphics").call(instance);
			if(out.isstring())
				return "tiles."+tileset+"."+out.checkjstring();
		}
		return getGraphicsPath(tileset);
	}

	public boolean collidesFrom(int dir,LuaTable instance,LuaValue player) {
		if((this.flags&HAS_COLLISION)!=0)
			return true;
		else if(this.enabledCollision) {
			LuaValue collide = base.get("checkCollision").call(base,instance,player);
			return collide.toboolean();
		}else if((this.flags&HAS_SEMICOLISSION)!=0)
			return !player.invokemethod("semiSolidClipEnabled").optvalue(1, LuaValue.NIL).toboolean();
		else
			return false;
	}

}
