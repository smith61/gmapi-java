package gmapi.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URL;

@Data
@EqualsAndHashCode( callSuper = false )
public final class Album extends GMObject {

    private String name;
    private String albumId;

    private String artist;
    private String[ ] artistId;

    private String albumArtist;
    private URL albumArtRef;

    private int year;
    private Track[ ] tracks;

    private String description;
    @SerializedName( "description_attribution" )
    private Attribution descriptionAttribution;

    private String explicitType;
    private String contentType;

    public Album( ) {
        super( Kind.ALBUM );
    }

}
