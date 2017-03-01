package gmapi.internal;

import java.util.concurrent.Future;

import gmapi.internal.protocol.APICall;

public interface AuthenticatedClient {

    < T > Future< T > doAuthedCall( APICall< T > call );

}
