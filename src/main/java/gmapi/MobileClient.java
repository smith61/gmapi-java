package gmapi;

import static gmapi.internal.utils.Validation.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import gmapi.internal.protocol.APICall;
import gmapi.internal.protocol.mobileclient.DeviceInfoCall;
import gmapi.internal.protocol.mobileclient.Factory;
import gmapi.internal.protocol.mobileclient.SongBytesCall;
import gmapi.internal.protocol.mobileclient.SongStreamCall;
import gmapi.internal.protocol.mobileclient.TrackCall;
import gmapi.models.DeviceInfo;
import gmapi.models.Track;

public class MobileClient {
	
	public static Future< MobileClient > newMobileClient( String email, String password, String androidID, ExecutorService executor ) {
		return executor.submit( new Factory( email, password, androidID, executor ) );
	}
	
	private final OkHttpClient client;
	private final ExecutorService executor;
	
	private final String androidID;
	private final String oauthToken;
	
	
	public MobileClient( OkHttpClient client, ExecutorService executor, String androidID, String oauthToken ) {
		this.client = checkNotNull( client, "client" );
		this.executor = checkNotNull( executor, "executor" );
		
		this.androidID = checkNotNull( androidID, "androidID" );
		this.oauthToken = checkNotNull( oauthToken, "oauthToken" );
		
		client.interceptors( ).add( new Interceptor( ) {

			@Override
			public Response intercept( Chain chain ) throws IOException {
				Request.Builder builder = chain.request( ).newBuilder( );
				builder.header( "User-Agent", "gmapi-java/mobileclient" );
				builder.header( "X-Device-ID", MobileClient.this.androidID );
				builder.header( "Authorization", "GoogleLogin auth=" + MobileClient.this.oauthToken );
				
				
				return chain.proceed( builder.build( ) );
			}
			
		} );
	}
	
	public < T > Future< T > doCall( APICall< T > apiCall ) {
		checkNotNull( apiCall, "apiCall" );
		
		apiCall.setHttpClient( this.client );
		return this.executor.submit( apiCall );
	}
	
	public Future< Page< Track > > getTracks( ) {
		return getTracks( 1000 );
	}
	
	public Future< Page< Track > > getTracks( int pageSize ) {
		return this.doCall( new TrackCall( this, "", pageSize ) );
	}
	
	public Future< InputStream > getSongStream( String songID ) {
		return this.doCall( new SongStreamCall( songID ) );
	}
	
	public Future< byte[ ] > getSongBytes( String songID ) {
		return this.doCall( new SongBytesCall( songID ) );
	}
	
	public Future< List< DeviceInfo > > getDeviceInfo( ) {
		return this.doCall( new DeviceInfoCall( ) );
	}
	
}
