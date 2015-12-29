package gmapi;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

import gmapi.models.DeviceInfo;
import gmapi.models.Track;

public interface MobileClient {

	Future< Page< Track > > getTracks( );
	Future< Page< Track > > getTracks( int pageSize );
	
	Future< InputStream > getTrackStream( String trackID );
	Future< byte[ ] > getTrackBytes( String trackID );
	
	Future< List< DeviceInfo > > getDeviceInfo( );
	
}
