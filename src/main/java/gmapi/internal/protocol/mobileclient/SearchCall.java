package gmapi.internal.protocol.mobileclient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Response;
import gmapi.internal.protocol.APICall;
import gmapi.internal.utils.GoogleUtils;
import gmapi.internal.utils.JsonUtils;
import gmapi.models.GMObject;
import gmapi.models.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchCall extends APICall< List< SearchResult > > {

    private final String query;
    private final int maxResults;

    public SearchCall( String query, int maxResults ) {
        this.query = Objects.requireNonNull( query, "query" );
        this.maxResults = maxResults;
    }

    @Override
    protected List< SearchResult > parseResponse( Response response ) throws IOException {
        Gson gson = JsonUtils.newGson( );

        JsonObject root = gson.fromJson( response.body( ).charStream( ), JsonObject.class );
        JsonArray entries = root.getAsJsonArray( "entries" );

        List< SearchResult > results = new ArrayList< >( );
        for( JsonElement entry : entries ) {
            JsonObject asObject = entry.getAsJsonObject( );

            boolean bestResult = asObject.get( "best_result" ).getAsBoolean( );
            boolean navigationalResult = asObject.get( "navigational_result" ).getAsBoolean( );

            String valueKey = this.getEntryValueName( asObject );
            if( valueKey != null ) {
                GMObject gmObject = gson.fromJson( asObject.get( valueKey ), GMObject.class );
                results.add( new SearchResult( gmObject, bestResult, navigationalResult ) );
            }
        }

        return results;
    }

    @Override
    protected HttpUrl getUrl( ) {
        HttpUrl.Builder builder = HttpUrl.parse( GoogleUtils.MCLIENTS_SJ_URL ).newBuilder( );
        builder.addPathSegment( "query" );

        builder.addQueryParameter( "q", this.query );
        builder.addQueryParameter( "ct", "1,2,3,4,6,7,8,9" );
        if( this.maxResults > 0 ) {
            builder.addQueryParameter( "max-results", "" + this.maxResults );
        }

        return builder.build( );
    }

    @Override
    protected String getMethod( ) {
        return "GET";
    }

    private String getEntryValueName( JsonObject entry ) {
        switch( entry.get( "type" ).getAsInt( ) ) {
            case 1:
                return "track";
            case 2:
                return "artist";
            case 3:
                return "album";
            case 4:
                return "playlist";
            case 6:
                return "station";
            case 7:
                return "situation";
            case 8:
                return "youtube_video";
            case 9:
                return "series";
        }

        return null;
    }

}
