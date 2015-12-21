package gmapi.internal.protocol;

import static gmapi.internal.utils.Validation.assertTrue;
import static gmapi.internal.utils.Validation.checkNotNull;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public abstract class APICall< T > implements Callable< T > {

	private OkHttpClient httpClient;
	
	public final APICall< T > setHttpClient( OkHttpClient httpClient ) {
		assertTrue( this.httpClient == null, "Internal client already set." );
		this.httpClient = checkNotNull( httpClient, "httpClient" );
		
		return this;
	}
	
	public final T call( ) throws IOException {
		assertTrue( this.httpClient != null, "Internal client not set." );
		
		Request.Builder builder = new Request.Builder( );
		builder.url( this.getUrl( ) );
		builder.headers( this.getHeaders( ).build( ) );
		builder.method( this.getMethod( ), this.getRequestBody( ) );
		
		Request request = builder.build( );
		Call call = this.httpClient.newCall( request );
		Response response = call.execute( );
		
		if( !response.isSuccessful( ) ) {
			throw new IOException( "Bad request: " + response.body( ).string( ) );
		}
		
		return this.parseResponse( response );
	}
	
	protected abstract T parseResponse( Response response ) throws IOException;
	
	protected abstract HttpUrl getUrl( );
	protected abstract String getMethod( );
	
	protected Headers.Builder getHeaders( ) {
		return new Headers.Builder( );
	}
	
	protected RequestBody getRequestBody( ) {
		return null;
	}
	
}
