package gmapi.internal.utils;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonUtils {

	public static JsonDeserializer< URL > URL_DESERIALIZER = new URLDeserializer( );
		
	public static Gson newGson( ) {
		GsonBuilder builder = new GsonBuilder( );
		builder.registerTypeAdapter( URL.class, URL_DESERIALIZER );
		
		return builder.create( );
	}
	
	private static class URLDeserializer implements JsonDeserializer< URL > {

		@Override
		public URL deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException {
			String url = json.getAsJsonObject( ).get( "url" ).getAsString( );
			try {
				return new URL( url );
			}
			catch( MalformedURLException excep ) {
				throw new JsonParseException( "Invalid URL: " + url );
			}
		}
		
	}
	
}
