package gmapi.models;

import java.net.URL;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = false )
public final class Track extends GMObject {

    private String title;
    private String artist;
    private String album;
    private String albumArtist;

    private String genre;
    private String trackType;

    private URL[] albumArtRef;
    private URL[] artistArtRef;

    private String storeId;
    private String albumId;
    private String[] artistId;
    private String id;
    private String nid;

    private int year;
    private int discNumber;
    private int trackNumber;

    public Track( ) {
        super( Kind.TRACK );
    }

}
