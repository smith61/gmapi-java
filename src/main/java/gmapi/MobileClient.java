package gmapi;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

import gmapi.models.DeviceInfo;
import gmapi.models.Track;

public interface MobileClient {

	String getAndroidId( );
	String getAccessToken( );

	Future< Page< Track > > getTracks( );
	Future< Page< Track > > getTracks( int pageSize );
	
	Future< InputStream > getTrackStream( String trackID );
	Future< byte[ ] > getTrackBytes( String trackID );
	
	Future< InputStream > getTrackStream( Track track );
	Future< byte[ ] > getTrackBytes( Track track );
	
	Future< List< DeviceInfo > > getDeviceInfo( );
	
	Future< Page< Track > > getPromotedTracks( );
	Future< Page< Track > > getPromotedTracks( int pageSize );

}
