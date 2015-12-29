package gmapi;

import static gmapi.internal.utils.Validation.checkNotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import gmapi.internal.mobileclient.Factory;

public class Clients {

	public static Future< MobileClient > newMobileClient( ExecutorService service, String email, String password, String androidID ) {
		checkNotNull( service, "service" );
		checkNotNull( email, "email" );
		checkNotNull( password, "password" );
		checkNotNull( androidID, "androidID" );
		
		return service.submit( new Factory( email, password, androidID, service ) );
	}
	
}
