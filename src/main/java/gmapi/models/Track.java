package gmapi.models;

public class Track {

	private String album;
	private String title;
	private String genre;
	private String artist;
	private String id;
	
	public Track( ) {
		this.album  = "Unknown";
		this.title  = "Unknown";
		this.genre  = "Unknown";
		this.artist = "Unknown";
		this.id     = "Unknown";
	}

	public String getAlbum( ) {
		return album;
	}

	public void setAlbum( String album ) {
		this.album = album;
	}

	public String getTitle( ) {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getGenre( ) {
		return genre;
	}

	public void setGenre( String genre ) {
		this.genre = genre;
	}

	public String getArtist( ) {
		return artist;
	}

	public void setArtist( String artist ) {
		this.artist = artist;
	}

	public String getId( ) {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Track [album=" + album + ", title=" + title + ", genre=" + genre + ", artist=" + artist + ", id=" + id
				+ "]";
	}
	
}
