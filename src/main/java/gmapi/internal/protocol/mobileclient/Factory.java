package gmapi.internal.protocol.mobileclient;

import static gmapi.internal.utils.Validation.checkNotNull;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.OkHttpClient;

import gmapi.AuthenticationException;
import gmapi.MobileClient;

public class Factory implements Callable< MobileClient > {

	private final String email;
	private final String password;
	private final String androidID;
	private final ExecutorService executor;
	
	public Factory( String email, String password, String androidID, ExecutorService executor ) {
		this.email = checkNotNull( email, "email" );
		this.password = checkNotNull( password, "password" );
		this.androidID = checkNotNull( androidID, "androidID" );
		this.executor = checkNotNull( executor, "executor" );
	}
	
	@Override
	public MobileClient call( ) throws AuthenticationException {
		try {
			OkHttpClient client = new OkHttpClient( );
			client.setDispatcher( new Dispatcher( this.executor ) );
			
			Map< String, String > res = this.makeLoginCall( client );
			if( !res.containsKey( "Token" ) ) {
				throw new AuthenticationException( );
			}
			String masterToken = res.get( "Token" );
			
			res = this.makeOauthCall( client, masterToken );
			if( !res.containsKey( "Auth" ) ) {
				throw new AuthenticationException( );
			}
			String oauthToken = res.get( "Auth" );
			
			return new MobileClient( client, this.executor, this.androidID, oauthToken );
		}
		catch( IOException ioe ) {
			throw new AuthenticationException( ioe );
		}
	}

	private Map< String, String > makeLoginCall( OkHttpClient client ) throws IOException {
		LoginCall loginCall = new LoginCall( this.email, this.password, this.androidID );
		loginCall.setHttpClient( client );
		
		return loginCall.call( );
	}
	
	private Map< String, String > makeOauthCall( OkHttpClient client, String masterToken ) throws IOException {
		OAuthCall oauthCall = new OAuthCall( this.androidID, masterToken );
		oauthCall.setHttpClient( client );
		
		return oauthCall.call( );
	}
	
}
