package gmapi.internal.protocol;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import gmapi.Page;
import gmapi.internal.AuthenticatedClient;

public class APIPage< T > implements Page< T > {

	private final AuthenticatedClient client;
	private final APIPageCall.Info< T > callInfo;
	
	private final String nextPageToken;
	private final int pageSize;
	
	private final List< T > currentPage;
	
	public APIPage( AuthenticatedClient client, APIPageCall.Info< T > callInfo, String nextPageToken, int pageSize, List< T > currentPage ) {
		this.client = client;
		this.callInfo = callInfo;
		
		this.nextPageToken = nextPageToken;
		this.pageSize = pageSize;
		
		this.currentPage = currentPage;
	}
	
	@Override
	public List< T > getElements( ) {
		return Collections.unmodifiableList( this.currentPage );
	}

	@Override
	public boolean hasNext( ) {
		return !( this.nextPageToken == null || this.nextPageToken.isEmpty( ) );
	}

	@Override
	public Future< Page< T > > next( ) {
		if( !this.hasNext( ) ) {
			throw new IllegalStateException( "No new page." );
		}
		
		return this.client.doAuthedCall( new APIPageCall< T >( this.client, this.callInfo, this.nextPageToken, this.pageSize ) );
	}

}
