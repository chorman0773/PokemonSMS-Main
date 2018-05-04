package com.google.sites.clibonlineprogram.pokemonsms.net.auth;

import java.util.UUID;

import com.google.gson.JsonObject;

public class SentryAccount implements PermissionsFlags {
private final UUID id;
private final String name;
private long permissions;

public SentryAccount(JsonObject obj) {
	id = UUID.fromString(obj.get("UUID").getAsString());
	name = obj.get("Username").getAsString();
	permissions = obj.get("Permissions").getAsLong();
}
public void setLocalPermissions(long permissions) {
	this.permissions = (permissions&~GLOBAL_FLAGS)|(this.permissions&GLOBAL_FLAGS);
}
public void loadPermissionsFromSentry(JsonObject obj) {
	this.permissions = (permissions&~GLOBAL_FLAGS)|(obj.get("Permissions").getAsLong()&GLOBAL_FLAGS);
}

public boolean canConnectToServer() {
	return (permissions&EITHER_BANNED)!=0;
}
public int donationPermissionLevel() {
	if((permissions&PERMISSION_ULTIMATE)!=0)
		return 3;
	else if((permissions&PERMISSION_PREMIUM)!=0)
		return 2;
	else if((permissions&PERMISSION_PRIORITY)!=0)
		return 1;
	return 0;
}
public int operatorPermissionLevel() {
	if((permissions&OWNER)!=0)
		return 4;
	else if((permissions&ADMIN)!=0)
		return 3;
	else if((permissions&MOD)!=0)
		return 2;
	else if((permissions&HELPER)!=0)
		return 1;
	else
		return 0;
}

public int getPriority() {
	if((permissions&OWNER)!=0)
		return Integer.MAX_VALUE;
	else if((permissions&PERMISSION_GLOBAL_ADMIN)!=0)
		return 1000000;
	else if((permissions&ADMIN)!=0)
		return 10000;
	else if((permissions&MOD)!=0)
		return 500;
	else if((permissions&HELPER)!=0)
		return 100;
	else if((permissions&PERMISSION_ULTIMATE)!=0)
		return 3;
	else if((permissions&PERMISSION_PREMIUM)!=0)
		return 2;
	else if((permissions&PERMISSION_PRIORITY)!=0)
		return 1;
	else
		return 0;
}

}
