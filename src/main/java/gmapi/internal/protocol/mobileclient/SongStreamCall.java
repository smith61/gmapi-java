package gmapi.internal.protocol.mobileclient;

import java.io.IOException;
import java.io.InputStream;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Response;

import gmapi.internal.protocol.APICall;
import gmapi.internal.utils.GoogleUtils;

public class SongStreamCall extends APICall< InputStream > {

	private final String songID;
	
	public SongStreamCall( String songID ) {
		this.songID = songID;
	}
	
	@Override
	protected InputStream parseResponse( Response response ) throws IOException {
		return response.body( ).byteStream( );
	}

	@Override
	protected HttpUrl getUrl( ) {
		HttpUrl.Builder builder = HttpUrl.parse( GoogleUtils.ANDROID_CLIENT_URL ).newBuilder( );
		builder.addPathSegment( "music" ).addPathSegment( "mplay" );
		
		String salt = Long.toString( System.currentTimeMillis( ) );
		String signature = GoogleUtils.getSongSignature( this.songID, salt );
		
		builder.addQueryParameter( "opt", "hi" );
		builder.addQueryParameter( "net", "mob" );
		builder.addQueryParameter( "pt",  "e" );
		builder.addQueryParameter( "slt", salt );
		builder.addQueryParameter( "sig", signature );
		
		if( this.songID.startsWith( "T" ) ) {
			builder.addQueryParameter( "mjck", this.songID );
		}
		else {
			builder.addQueryParameter( "songid", this.songID );
		}
		
		return builder.build( );
	}

	@Override
	protected String getMethod( ) {
		return "GET";
	}

}
