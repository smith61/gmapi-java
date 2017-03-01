package gmapi.internal.protocol.mobileclient;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import gmapi.internal.protocol.APICall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class GPSCall extends APICall< Map< String, String > > {

    private final String androidID;

    protected GPSCall( String androidID ) {
        this.androidID = Objects.requireNonNull( androidID, "androidID" );
    }

    @Override
    protected final Map< String, String > parseResponse( Response response ) throws IOException {
        Map< String, String > v = new HashMap< >( );
        try( BufferedReader reader = new BufferedReader( response.body( ).charStream( ) ) ) {
            String line;
            while( ( line = reader.readLine( ) ) != null ) {
                line = line.trim( );

                int i = line.indexOf( '=' );
                if( i == -1 ) continue;

                v.put( line.substring( 0, i ), line.substring( i + 1 ) );
            }
        }
        return v;
    }

    @Override
    protected final RequestBody getRequestBody( ) {
        StringBuilder encodedForm = new StringBuilder( );
        for( Map.Entry< String, String > formParam : this.getFormParams( ).entrySet( ) ) {
            String key = GPSCall.encode( formParam.getKey( ) );
            String val = GPSCall.encode( formParam.getValue( ) );

            if( encodedForm.length( ) != 0 ) {
                encodedForm.append( '&' );
            }
            encodedForm.append( key ).append( '=' ).append( val );
        }

        return RequestBody.create( MediaType.parse( "application/x-www-form-urlencoded" ), encodedForm.toString( ) );
    }

    @Override
    protected final String getMethod( ) {
        return "POST";
    }

    protected Map< String, String > getFormParams( ) {
        Map< String, String > formParams = new HashMap< >( );

        formParams.put( "accountType", "HOSTED_OR_GOOGLE" );
        formParams.put( "has_permission", "1" );
        formParams.put( "source", "android" );
        formParams.put( "device_country", "us" );
        formParams.put( "operatorCountry", "us" );
        formParams.put( "lang", "en" );
        formParams.put( "sdk_version", "17" );
        formParams.put( "androidId", this.androidID );

        return formParams;
    }

    private static String encode( String s ) {
        try {
            return URLEncoder.encode( s, "UTF-8" );
        }
        catch( UnsupportedEncodingException e ) {
            throw new AssertionError( e );
        }
    }


}
