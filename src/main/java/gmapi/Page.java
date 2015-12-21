package gmapi;

import java.util.concurrent.Future;

public interface Page< T > {

	T get( int index );
	int size( );
	
	boolean hasNext( );
	Future< Page< T > > next( );
	
}
