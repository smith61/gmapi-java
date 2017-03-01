package gmapi;

import java.util.List;
import java.util.concurrent.Future;

public interface Page< T > {

    List< T > getElements( );

    boolean hasNext( );

    Future< Page< T > > next( );

}
