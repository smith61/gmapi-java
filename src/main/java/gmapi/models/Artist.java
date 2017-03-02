package gmapi.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URL;

@Data
@EqualsAndHashCode( callSuper = false )
public final class Artist extends GMObject {

    private String name;

    private URL artistArtRef;
    private Image[ ] artistArtRefs;
    @SerializedName( "artist_bio_attribution" )
    private Attribution artistBioAttribution;

    private String artistBio;
    private String artistId;

    private Album[ ] albums;
    private Track[ ] topTracks;

    @SerializedName( "total_albums" )
    private int totalAlbums;

    @SerializedName( "related_artists" )
    private Artist[ ] relatedArtists;

    public Artist( ) {
        super( Kind.ARTIST );
    }

}
