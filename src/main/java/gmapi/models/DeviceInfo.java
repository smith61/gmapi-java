package gmapi.models;

import lombok.Data;

@Data
public class DeviceInfo {

    private String id;
    private String friendlyName;
    private String type;
    private long lastAccessTimeMs;

}
