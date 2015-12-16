package gmapi.internal.utils;

public class Validation {

	public static void assertTrue( boolean val, String format, Object... args ) {
		if( !val ) {
			throw new AssertionError( String.format( format, args ) );
		}
	}
	
	public static < T > T checkNotNull( T t, String name ) {
		if( t == null ) {
			throw new NullPointerException( String.format( "'%s' can not be null.", name ) );
		}
		return t;
	}
	
}
