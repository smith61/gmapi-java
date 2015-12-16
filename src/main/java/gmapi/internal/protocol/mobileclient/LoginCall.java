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

public class LoginCall extends APICall< Map< String, String > > {

	private final String email;
	private final String password;
	private final String androidID;
	
	public LoginCall( String email, String password, String androidID ) {
		this.email = checkNotNull( email, "email" );
		this.password = checkNotNull( password, "password" );
		this.androidID = checkNotNull( androidID, "androidID" );
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
	protected RequestBody getRequestBody( ) {
		Map< String, String > formParams = new HashMap< >( );
		
		formParams.put( "accountType", "HOSTED_OR_GOOGLE" );
		formParams.put( "has_permission", "1" );
		formParams.put( "add_account", "1" );
		formParams.put( "service", "ac2dm" );
		formParams.put( "source", "android" );
		formParams.put( "device_country", "us" );
		formParams.put( "operatorCountry", "us" );
		formParams.put( "lang", "en" );
		formParams.put( "sdk_version", "17" );
		formParams.put( "Email", this.email );
		formParams.put( "EncryptedPasswd", GoogleUtils.gpsOauthSignature( this.email, this.password ) );
		formParams.put( "androidID", this.androidID );
		
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

	private static String encode( String s ) {
		try {
			return URLEncoder.encode( s, "UTF-8" );
		}
		catch( UnsupportedEncodingException e ) {
			throw new AssertionError( e );
		}
	}
	
}
