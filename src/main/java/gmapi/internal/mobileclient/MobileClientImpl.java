package gmapi.internal.mobileclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import gmapi.MobileClient;
import gmapi.Page;
import gmapi.internal.AuthenticatedClient;
import gmapi.internal.protocol.APICall;
import gmapi.internal.protocol.APIPageCall;
import gmapi.internal.protocol.mobileclient.DeviceInfoCall;
import gmapi.internal.protocol.mobileclient.SongBytesCall;
import gmapi.internal.protocol.mobileclient.SongStreamCall;
import gmapi.internal.protocol.mobileclient.TrackCallInfo;
import gmapi.models.DeviceInfo;
import gmapi.models.Track;

public class MobileClientImpl implements AuthenticatedClient, MobileClient {

	private final OkHttpClient httpClient;
	private final ExecutorService executorService;
	
	protected MobileClientImpl( OkHttpClient httpClient, ExecutorService service, final String androidID, final String oauthToken ) {
		this.httpClient = httpClient;
		this.executorService = service;
		
		this.httpClient.interceptors( ).add( new Interceptor( ) {
			
			@Override
			public Response intercept( Chain chain ) throws IOException {
				Request.Builder builder = chain.request( ).newBuilder( );
				builder.header( "User-Agent", "gmapi-java/mobileclient" );
				builder.header( "X-Device-ID", androidID );
				builder.header( "Authorization", "GoogleLogin auth=" + oauthToken );
				
				
				return chain.proceed( builder.build( ) );
			}
			
		} );
	}
	
	public < T > Future< T > doAuthedCall( APICall< T > apiCall ) {
		apiCall.setHttpClient( this.httpClient );
		return this.executorService.submit( apiCall );
	}
	
	@Override
	public Future< Page< Track > > getTracks( ) {
		return this.getTracks( 10000 );
	}

	@Override
	public Future< Page< Track > > getTracks( int pageSize ) {
		return this.doAuthedCall( new APIPageCall< Track >( this, new TrackCallInfo( ), pageSize ) );
	}

	@Override
	public Future< InputStream > getTrackStream( String trackID ) {
		return this.doAuthedCall( new SongStreamCall( trackID ) );
	}

	@Override
	public Future< byte[ ] > getTrackBytes( String trackID ) {
		return this.doAuthedCall( new SongBytesCall( trackID ) );
	}

	@Override
	public Future< List< DeviceInfo > > getDeviceInfo( ) {
		return this.doAuthedCall( new DeviceInfoCall( ) );
	}

}
