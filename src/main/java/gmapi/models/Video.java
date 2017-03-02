package gmapi.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = false )
public final class Video extends GMObject {

    private String id;
    private String title;

    private Thumbnail[ ] thumbnails;

    public Video( ) {
        super( Kind.VIDEO );
    }

}
