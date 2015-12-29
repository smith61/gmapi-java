package gmapi.internal.protocol.mobileclient;

import static gmapi.internal.utils.Validation.checkNotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import gmapi.Page;
import gmapi.internal.mobileclient.MobileClientImpl;
import gmapi.models.Track;

public class TrackPage implements Page< Track > {

	private final MobileClientImpl client;
	
	private final String nextPageToken;
	private final int pageSize;
	
	private final List< Track > currentPage;
	
	public TrackPage( MobileClientImpl client, String nextPageToken, int pageSize, List< Track > currentPage ) {
		this.client = checkNotNull( client, "client" );
		
		this.nextPageToken = checkNotNull( nextPageToken, "nextPageToken" );
		this.pageSize = checkNotNull( pageSize, "pageSize" );
		
		this.currentPage = Collections.unmodifiableList( checkNotNull( currentPage, "currentPage" ) );
	}
	
	@Override
	public List< Track > getElements( ) {
		return this.currentPage;
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
		
		return this.client.doAuthedCall( new TrackCall( this.client, this.nextPageToken, this.pageSize ) );
	}

	
	
}
