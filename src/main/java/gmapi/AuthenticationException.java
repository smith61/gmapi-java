package gmapi;

public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = -2536168500347803053L;

    public AuthenticationException( ) {
        super( "Invalid email/password combination." );
    }

    public AuthenticationException( Throwable cause ) {
        super( "Error while authenticating.", cause );
    }

}
