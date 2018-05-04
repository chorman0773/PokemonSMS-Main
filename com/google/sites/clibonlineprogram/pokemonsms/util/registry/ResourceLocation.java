package com.google.sites.clibonlineprogram.pokemonsms.util.registry;

public class ResourceLocation implements Comparable<ResourceLocation> {
private String domain;
private String path;
public ResourceLocation(String base) {
	String[] parts = base.split(":");
	domain = parts[0];
	path = parts[1];
}
public ResourceLocation(String domain,String path) {
	this.domain = domain;
	this.path = path;
}
/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((domain == null) ? 0 : domain.hashCode());
	result = prime * result + ((path == null) ? 0 : path.hashCode());
	return result;
}
/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
@Override
public boolean equals(Object obj) {
	if (this == obj) {
		return true;
	}
	if (obj == null) {
		return false;
	}
	if (!(obj instanceof ResourceLocation)) {
		return false;
	}
	ResourceLocation other = (ResourceLocation) obj;
	if (domain == null) {
		if (other.domain != null) {
			return false;
		}
	} else if (!domain.equalsIgnoreCase(other.domain)) {
		return false;
	}
	if (path == null) {
		if (other.path != null) {
			return false;
		}
	} else if (!path.equalsIgnoreCase(other.path)) {
		return false;
	}
	return true;
}
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return domain+":"+path;
}

public String getDomain() {
	return domain;
}
public String getPath() {
	return path;
}
@Override
public int compareTo(ResourceLocation o) {
	int domains = domain.compareToIgnoreCase(o.domain);
	if(domains!=0)
		return domains;
	else
		return path.compareToIgnoreCase(o.domain);
}

}
