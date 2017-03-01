package gmapi.internal.protocol.mobileclient;

import java.util.Map;
import java.util.Objects;

import com.squareup.okhttp.HttpUrl;

import gmapi.internal.utils.GoogleUtils;

public class OAuthCall extends GPSCall {

    private final String masterToken;

    public OAuthCall( String androidID, String masterToken ) {
        super( androidID );

        this.masterToken = Objects.requireNonNull( masterToken, "masterToken" );
    }

    @Override
    protected HttpUrl getUrl( ) {
        HttpUrl.Builder builder = HttpUrl.parse( GoogleUtils.ANDROID_CLIENT_URL ).newBuilder( );
        builder.addPathSegment( "auth" );

        return builder.build( );
    }

    @Override
    protected Map< String, String > getFormParams( ) {
        Map< String, String > formParams = super.getFormParams( );

        formParams.put( "service", "sj" );
        formParams.put( "app", "com.google.android.music" );
        formParams.put( "client_sig", "38918a453d07199354f8b19af05ec6562ced5788" );
        formParams.put( "EncryptedPasswd", this.masterToken );

        return formParams;
    }

}
