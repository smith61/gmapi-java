package gmapi.models;

import java.net.URL;

import lombok.Data;

@Data
public class Track {

	private String kind;
	
	private String title;
	private String artist;
	private String album;
	private String albumArtist;
	
	private String genre;
	private String trackType;
	
	private URL[ ] albumArtRef;
	private URL[ ] artistArtRef;
	
	private String storeId;
	private String albumId;
	private String[ ] artistId;
	private String id;
	private String nid;
	
	private int year;
	private int discNumber;
	private int trackNumber;
	
	public Track( ) {
		this.kind         = "Unknown";
		this.title        = "Unknown";
		this.artist       = "Unknown";
		this.albumArtist  = "Unknown";
		
		this.genre        = "Unknown";
		this.trackType    = "Unknown";
		
		this.albumArtRef  = new URL[ 0 ];
		this.artistArtRef = new URL[ 0 ];
		
		this.storeId      = "";
		this.albumId      = "";
		this.artistId     = new String[ 0 ];
		this.id           = "";
		this.nid          = "";
		
		this.year        = -1;
		this.discNumber  = -1;
		this.trackNumber = -1;
	}

}
