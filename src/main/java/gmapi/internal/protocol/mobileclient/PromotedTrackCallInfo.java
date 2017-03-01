package gmapi.internal.protocol.mobileclient;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.HttpUrl;

import gmapi.internal.protocol.APIPageCall;
import gmapi.internal.utils.GoogleUtils;
import gmapi.models.Track;

public class PromotedTrackCallInfo implements APIPageCall.Info< Track > {

    @Override
    public TypeToken< List< Track > > getTypeToken( ) {
        return new TypeToken< List< Track > >( ) {
        };
    }

    @Override
    public HttpUrl getUrl( ) {
        HttpUrl.Builder builder = HttpUrl.parse( GoogleUtils.MCLIENTS_SJ_URL ).newBuilder( );
        builder.addPathSegment( "ephemeral" ).addPathSegment( "top" );

        return builder.build( );
    }

}
