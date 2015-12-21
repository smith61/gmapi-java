package gmapi.models;

public class DeviceInfo {

	private String id;
	private String friendlyName;
	private String type;
	private long lastAccessTimeMs;
	
	public DeviceInfo( ) {
		this.id = "Unknown";
		this.friendlyName = "Unknown";
		this.type = "Unknown";
		this.lastAccessTimeMs = 0;
	}

	public String getId( ) {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public String getFriendlyName( ) {
		return friendlyName;
	}

	public void setFriendlyName( String friendlyName ) {
		this.friendlyName = friendlyName;
	}

	public String getType( ) {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public long getLastAccessTimeMs( ) {
		return lastAccessTimeMs;
	}

	public void setLastAccessTimeMs( long lastAccessTimeMs ) {
		this.lastAccessTimeMs = lastAccessTimeMs;
	}

	@Override
	public String toString( ) {
		return "DeviceInfo [id=" + id + ", friendlyName=" + friendlyName + ", type=" + type + ", lastAccessTimeMs="
				+ lastAccessTimeMs + "]";
	}
	
}
