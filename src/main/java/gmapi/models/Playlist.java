package gmapi.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URL;

@Data
@EqualsAndHashCode( callSuper = false )
public final class Playlist extends GMObject {

    private String id;
    private String name;
    private String description;

    private String explicitType;
    private String contentType;

    private boolean deleted;
    private boolean accessControlled;

    private Type type;

    private String lastModifiedTimestamp;
    private String recentTimestamp;
    private String creationTimestamp;

    private String shareToken;
    private ShareState shareState;

    private URL ownerProfilePhotoUrl;
    private String ownerName;

    private URL[ ] albumArtRef;


    public Playlist( ) {
        super( Kind.PLAYLIST );

        this.type = Type.UNKNOWN;

        this.shareState = ShareState.UNKNOWN;
    }

    public enum Type {
        MAGIC,
        SHARED,
        USER_GENERATED,
        UNKNOWN
    }

    public enum ShareState {
        PRIVATE,
        PUBLIC,
        UNKNOWN
    }

}
