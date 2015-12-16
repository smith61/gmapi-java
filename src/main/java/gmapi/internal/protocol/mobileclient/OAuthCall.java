package gmapi.internal.protocol.mobileclient;

import static gmapi.internal.utils.Validation.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import gmapi.internal.protocol.APICall;
import gmapi.internal.utils.GoogleUtils;

public class OAuthCall extends APICall< Map< String, String > > {

	private final String androidID;
	private final String masterToken;
	
	public OAuthCall( String androidID, String masterToken ) {
		this.androidID = checkNotNull( androidID, "androidID" );
		this.masterToken = checkNotNull( masterToken, "masterToken" );
	}
	
	@Override
	protected Map< String, String > parseResponse( Response response ) throws IOException {
		Map< String, String > v = new HashMap< >( );
		try( BufferedReader reader = new BufferedReader( response.body( ).charStream( ) ) ) {
				String line;
				while( ( line = reader.readLine( ) ) != null ) {
					int i = line.trim( ).indexOf( '=' );
					if( i == -1 ) continue;
					
					v.put( line.substring( 0, i ), line.substring( i + 1 ) );
				}
		}
		return v;
	}

	@Override
	protected HttpUrl getUrl( ) {
		HttpUrl.Builder builder = HttpUrl.parse( GoogleUtils.ANDROID_CLIENT_URL ).newBuilder( );
		builder.addPathSegment( "auth" );
		
		return builder.build( );
	}

	@Override
	protected String getMethod( ) {
		return "POST";
	}

	@Override
	protected RequestBody getRequestBody( ) {
		Map< String, String > formParams = new HashMap< >( );
		
		formParams.put( "accountType", "HOSTED_OR_GOOGLE" );
		formParams.put( "has_permission", "1" );
		formParams.put( "service", "sj" );
		formParams.put( "source", "android" );
		formParams.put( "app", "com.google.android.music" );
		formParams.put( "device_country", "us" );
		formParams.put( "operatorCountry", "us" );
		formParams.put( "lang", "en" );
		formParams.put( "sdk_version", "17" );
		formParams.put( "client_sig", "38918a453d07199354f8b19af05ec6562ced5788" );
		formParams.put( "androidID", this.androidID );
		formParams.put( "EncryptedPasswd", this.masterToken );
		
		StringBuilder encodedForm = new StringBuilder( );
		for( Entry< String, String > formParam : formParams.entrySet( ) ) {
			String key = encode( formParam.getKey( ) );
			String val = encode( formParam.getValue( ) );
			
			if( encodedForm.length( ) != 0 ) {
				encodedForm.append( '&' );
			}
			encodedForm.append( key ).append( '=' ).append( val );
		}
		
		return RequestBody.create( MediaType.parse( "application/x-www-form-urlencoded" ), encodedForm.toString( ) );
	}

	private static String encode( String s ) {
		try {
			return URLEncoder.encode( s, "UTF-8" );
		}
		catch( UnsupportedEncodingException e ) {
			throw new AssertionError( e );
		}
	}
	
}
