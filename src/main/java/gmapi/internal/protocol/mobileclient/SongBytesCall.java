package gmapi.internal.protocol.mobileclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Response;

import gmapi.internal.protocol.APICall;

public class SongBytesCall extends APICall< byte[] > {

    private final SongStreamCall streamCall;

    public SongBytesCall( String songID ) {
        this.streamCall = new SongStreamCall( songID );
    }

    @Override
    protected byte[] parseResponse( Response response ) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream( );
        InputStream in = this.streamCall.parseResponse( response );

        int read = 0;
        byte[] buf = new byte[ 4096 ];
        while( ( read = in.read( buf ) ) != -1 ) {
            out.write( buf, 0, read );
        }

        return out.toByteArray( );
    }

    @Override
    protected HttpUrl getUrl( ) {
        return this.streamCall.getUrl( );
    }

    @Override
    protected String getMethod( ) {
        return this.streamCall.getMethod( );
    }

}
