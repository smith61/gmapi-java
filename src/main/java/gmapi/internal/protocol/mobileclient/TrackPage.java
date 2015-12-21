package gmapi.internal.protocol.mobileclient;

import java.util.List;
import java.util.concurrent.Future;

import gmapi.MobileClient;
import gmapi.Page;
import gmapi.models.Track;

public class TrackPage implements Page< Track > {

	private final MobileClient client;
	
	private final String nextPageToken;
	private final int pageSize;
	
	private final List< Track > currentPage;
	
	public TrackPage( MobileClient client, String nextPageToken, int pageSize, List< Track > currentPage ) {
		this.client = client;
		
		this.nextPageToken = nextPageToken;
		this.pageSize = pageSize;
		
		this.currentPage = currentPage;
	}
	
	@Override
	public Track get( int index ) {
		return this.currentPage.get( index );
	}

	@Override
	public int size( ) {
		return this.currentPage.size( );
	}

	@Override
	public boolean hasNext( ) {
		return !( this.nextPageToken == null || this.nextPageToken.isEmpty( ) );
	}

	@Override
	public Future< Page< Track > > next( ) {
		if( !this.hasNext( ) ) {
			throw new IllegalStateException( "No new page." );
		}
		
		return this.client.doCall( new TrackCall( this.client, this.nextPageToken, this.pageSize ) );
	}

	
	
}
