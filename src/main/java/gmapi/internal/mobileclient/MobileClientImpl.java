package gmapi.internal.mobileclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import gmapi.MobileClient;
import gmapi.Page;
import gmapi.internal.AuthenticatedClient;
import gmapi.internal.protocol.APICall;
import gmapi.internal.protocol.APIPageCall;
import gmapi.internal.protocol.mobileclient.DeviceInfoCall;
import gmapi.internal.protocol.mobileclient.PromotedTrackCallInfo;
import gmapi.internal.protocol.mobileclient.SearchCall;
import gmapi.internal.protocol.mobileclient.SongBytesCall;
import gmapi.internal.protocol.mobileclient.SongStreamCall;
import gmapi.internal.protocol.mobileclient.TrackCallInfo;
import gmapi.models.DeviceInfo;
import gmapi.models.SearchResult;
import gmapi.models.Track;

public class MobileClientImpl implements AuthenticatedClient, MobileClient {

    private final OkHttpClient httpClient;
    private final ExecutorService executorService;

    private final String androidId;
    private final String accessToken;

    protected MobileClientImpl( OkHttpClient httpClient, ExecutorService service, final String androidID, final String oauthToken ) {
        this.httpClient = httpClient;
        this.executorService = service;

        this.androidId = androidID;
        this.accessToken = oauthToken;

        this.httpClient.interceptors( ).add( new Interceptor( ) {

            @Override
            public Response intercept( Chain chain ) throws IOException {
                Request.Builder builder = chain.request( ).newBuilder( );
                builder.header( "User-Agent", "gmapi-java/mobileclient" );
                builder.header( "X-Device-ID", androidID );
                builder.header( "Authorization", "GoogleLogin auth=" + oauthToken );

                HttpUrl.Builder urlBuilder = chain.request( ).httpUrl( ).newBuilder( );
                urlBuilder.addQueryParameter( "dv", "0" );
                urlBuilder.addQueryParameter( "hl", "en_US" );
                urlBuilder.addQueryParameter( "tier", "aa" );
                builder.url( urlBuilder.build( ) );

                return chain.proceed( builder.build( ) );
            }

        } );
    }

    @Override
    public String getAndroidId( ) {
        return this.androidId;
    }

    @Override
    public String getAccessToken( ) {
        return this.accessToken;
    }

    public < T > Future< T > doAuthedCall( APICall< T > apiCall ) {
        apiCall.setHttpClient( this.httpClient );
        return this.executorService.submit( apiCall );
    }

    @Override
    public Future< Page< Track > > getTracks( ) {
        return this.getTracks( 10000 );
    }

    @Override
    public Future< Page< Track > > getTracks( int pageSize ) {
        return this.doAuthedCall( new APIPageCall< >( this, new TrackCallInfo( ), pageSize ) );
    }

    @Override
    public Future< InputStream > getTrackStream( String trackID ) {
        return this.doAuthedCall( new SongStreamCall( trackID ) );
    }

    @Override
    public Future< byte[] > getTrackBytes( String trackID ) {
        return this.doAuthedCall( new SongBytesCall( trackID ) );
    }

    @Override
    public Future< InputStream > getTrackStream( Track track ) {
        return this.getTrackStream( track.getNid( ) );
    }

    @Override
    public Future< byte[] > getTrackBytes( Track track ) {
        return this.getTrackBytes( track.getNid( ) );
    }

    @Override
    public Future< List< DeviceInfo > > getDeviceInfo( ) {
        return this.doAuthedCall( new DeviceInfoCall( ) );
    }

    @Override
    public Future< Page< Track > > getPromotedTracks( ) {
        return this.getPromotedTracks( 10000 );
    }

    @Override
    public Future< Page< Track > > getPromotedTracks( int pageSize ) {
        return this.doAuthedCall( new APIPageCall< >( this, new PromotedTrackCallInfo( ), pageSize ) );
    }

    @Override
    public Future< List< SearchResult > > search( String query ) {
        return this.search( query, -1 );
    }

    @Override
    public Future< List< SearchResult > > search( String query, int maxResults ) {
        return this.doAuthedCall( new SearchCall( query, maxResults ) );
    }

}
