package gmapi.internal.protocol.mobileclient;

import static gmapi.internal.utils.Validation.checkNotNull;

import java.io.IOException;
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
import gmapi.internal.mobileclient.MobileClientImpl;
import gmapi.internal.protocol.APICall;
import gmapi.internal.utils.GoogleUtils;
import gmapi.internal.utils.JsonUtils;
import gmapi.models.Track;

public class TrackCall extends APICall< Page< Track > > {

	private final MobileClientImpl client;
	
	private final String pageToken;
	private final int pageSize;
	
	public TrackCall( MobileClientImpl client, String pageToken, int pageSize ) {		
		this.client = checkNotNull( client, "client" );
		
		this.pageToken = checkNotNull( pageToken, "pageToken" );
		this.pageSize = checkNotNull( pageSize, "pageSize" );
	}
	
	@Override
	protected Page< Track > parseResponse( Response response ) throws IOException {
		Gson gson = JsonUtils.newGson( );
		
		JsonObject root = gson.fromJson( response.body( ).charStream( ), JsonObject.class );
		
		String nextPageToken = "";
		
		JsonElement element = root.get( "nextPageToken" );
		if( element != null && !element.isJsonNull( ) ) {
			nextPageToken = element.getAsString( );
		}
		
		element = root.get( "data" ).getAsJsonObject( ).get( "items" );
		
		List< Track > tracks = gson.fromJson( element, new TypeToken< List< Track > >( ) { }.getType( ) );
		
		return new TrackPage( this.client, nextPageToken, this.pageSize, tracks );
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
		HttpUrl.Builder builder = HttpUrl.parse( GoogleUtils.SKYJAM_URL ).newBuilder( );
		builder.addPathSegment( "trackfeed" );
		
		return builder.build( );
	}

	@Override
	protected String getMethod( ) {
		return "POST";
	}
	
}
