package gmapi.internal.utils;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gmapi.models.GMObject;

public class JsonUtils {

    public static JsonDeserializer< URL > URL_DESERIALIZER = new URLDeserializer( );
    public static JsonDeserializer< GMObject > GMOBJECT_DESERIALIZER = new GMObjectDeserializer( );

    public static Gson newGson( ) {
        GsonBuilder builder = new GsonBuilder( );
        builder.registerTypeAdapter( URL.class, URL_DESERIALIZER );
        builder.registerTypeAdapter( GMObject.class, GMOBJECT_DESERIALIZER );

        return builder.create( );
    }

    private static class URLDeserializer implements JsonDeserializer< URL > {

        @Override
        public URL deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException {
            String url;
            if( json.isJsonObject( ) ) {
                url = json.getAsJsonObject( ).get( "url" ).getAsString( );
            }
            else {
                url = json.getAsString( );
            }

            try {
                return new URL( url );
            }
            catch( MalformedURLException excep ) {
                throw new JsonParseException( "Invalid URL: " + url );
            }
        }

    }

    private static class GMObjectDeserializer implements JsonDeserializer< GMObject > {

        @Override
        public GMObject deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException {
            if( !json.isJsonObject( ) ) {
                throw new JsonParseException( "Expected: JsonObject, Got: " + json );
            }

            JsonObject asObject = json.getAsJsonObject( );
            GMObject.Kind kind = GMObject.Kind.parseKind( asObject.get( "kind" ).getAsString( ) );
            if( kind == GMObject.Kind.UNKNOWN ) {
                return new GMObject( );
            }

            return context.deserialize( asObject, kind.getModelClass( ) );
        }
    }

}
