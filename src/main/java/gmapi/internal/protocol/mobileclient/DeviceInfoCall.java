package gmapi.internal.protocol.mobileclient;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Response;

import gmapi.internal.protocol.APICall;
import gmapi.internal.utils.GoogleUtils;
import gmapi.models.DeviceInfo;

public class DeviceInfoCall extends APICall< List< DeviceInfo > > {

	@Override
	protected List< DeviceInfo > parseResponse( Response response ) throws IOException {
		Gson gson = new GsonBuilder( ).create( );
		
		JsonObject root = gson.fromJson( response.body( ).charStream( ), JsonObject.class );
		
		JsonElement element = root.get( "data" ).getAsJsonObject( ).get( "items" );
		return gson.fromJson( element, new TypeToken< List< DeviceInfo > >( ) { }.getType( ) );
	}

	@Override
	protected HttpUrl getUrl( ) {
		HttpUrl.Builder builder = HttpUrl.parse( GoogleUtils.SKYJAM_URL ).newBuilder( );
		builder.addPathSegment( "devicemanagementinfo" );
		
		return builder.build( );
	}

	@Override
	protected String getMethod( ) {
		return "GET";
	}
	
}
