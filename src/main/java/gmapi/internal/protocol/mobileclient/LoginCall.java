package gmapi.internal.protocol.mobileclient;

import java.util.Map;
import java.util.Objects;

import com.squareup.okhttp.HttpUrl;

import gmapi.internal.utils.GoogleUtils;

public class LoginCall extends GPSCall {

    private final String email;
    private final String password;

    public LoginCall( String email, String password, String androidID ) {
        super( androidID );

        this.email = Objects.requireNonNull( email, "email" );
        this.password = Objects.requireNonNull( password, "password" );
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

        formParams.put( "add_account", "1" );
        formParams.put( "service", "ac2dm" );
        formParams.put( "Email", this.email );
        formParams.put( "EncryptedPasswd", GoogleUtils.gpsOauthSignature( this.email, this.password ) );

        return formParams;
    }

}
