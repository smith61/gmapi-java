package gmapi.models;

import lombok.Data;

@Data
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

}
