package gmapi.internal.protocol;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import gmapi.Page;
import gmapi.internal.AuthenticatedClient;
import gmapi.internal.utils.JsonUtils;

public class APIPageCall< T > extends APICall< Page< T > > {

	private final AuthenticatedClient client;
	
	private final Info< T > callInfo;
	
	private final String pageToken;
	private final int pageSize;
	
	public APIPageCall( AuthenticatedClient client, Info< T > callInfo, int pageSize ) {
		this( client, callInfo, "", pageSize );
	}
	
	public APIPageCall( AuthenticatedClient client, Info< T > callInfo, String pageToken, int pageSize ) {
		this.client = client;
		
		this.callInfo = callInfo;
		
		this.pageToken = pageToken;
		this.pageSize = pageSize;
	}
	
	@Override
	protected Page< T > parseResponse( Response response ) throws IOException {
		Gson gson = JsonUtils.newGson( );
		
		JsonObject root = gson.fromJson( response.body( ).charStream( ), JsonObject.class );
		
		String nextPageToken = null;
		JsonElement element = root.get( "nextPageToken" );
		if( element != null && !element.isJsonNull( ) ) {
			nextPageToken = element.getAsString( );
		}
		
		List< T > result = this.parseResponse( root, gson );
		return new APIPage< T >( this.client, this.callInfo, nextPageToken, this.pageSize, result );
	}
	
	@Override
	protected RequestBody getRequestBody( ) {
		JsonObject obj = new JsonObject( );
		obj.addProperty( "max-results", this.pageSize );
		obj.addProperty( "start-token", this.pageToken );
		
		Gson gson = new Gson( );
		String json = gson.toJson( obj );
		
		return RequestBody.create( MediaType.parse( "application/json" ), json );
	}
	
	@Override
	protected HttpUrl getUrl( ) {
		return this.callInfo.getUrl( );
	}

	@Override
	protected String getMethod( ) {
		return "POST";
	}
	
	private List< T > parseResponse( JsonObject root, Gson gson ) {
		JsonElement element = root.get( "data" );
		if( element == null || !element.isJsonObject( ) ) {
			return Collections.emptyList( );
		}
		element = element.getAsJsonObject( ).get( "items" );
		if( element == null || element.isJsonNull( ) ) {
			return Collections.emptyList( );
		}
		return gson.fromJson( element, this.callInfo.getTypeToken( ).getType( ) );
	}

	public static interface Info< T > {
		
		TypeToken< List< T > > getTypeToken( );
		
		HttpUrl getUrl( );
		
	}
	
}
