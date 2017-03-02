package gmapi.models;

import java.util.Objects;

public class GMObject {

    private final Kind kind;

    public GMObject( ) {
        this( Kind.UNKNOWN );
    }

    protected GMObject( Kind kind ) {
        if( kind.getModelClass( ) != this.getClass( ) ) {
            String msg = "Invalid Kind provided to GMObject. Got: %s, Expected: %s";
            throw new AssertionError( String.format( msg, kind.getModelClass( ), this.getClass( ) ) );
        }
        this.kind = Objects.requireNonNull( kind, "kind" );
    }

    public Kind getKind( ) {
        return this.kind;
    }

    public enum Kind {
        ALBUM( "sj#album", Album.class ),
        ARTIST( "sj#artist", Artist.class ),
        ATTRIBUTION( "sj#attribution", Attribution.class ),
        IMAGE( "sj#image", Image.class ),
        PLAYLIST( "sj#playlist", Playlist.class ),
        TRACK( "sj#track", Track.class ),
        VIDEO( "sj#video", Video.class ),
        UNKNOWN( "sj#unknown", GMObject.class );

        private final String name;
        private final Class< ? extends GMObject > modelClass;

        Kind( String name, Class< ? extends GMObject > modelClass ) {
            this.name = name;
            this.modelClass = modelClass;
        }

        public String getName( ) {
            return this.name;
        }

        public Class< ? extends GMObject > getModelClass( ) {
            return this.modelClass;
        }

        public static Kind parseKind( String name ) {
            for( Kind kind : Kind.values( ) ) {
                if( kind.getName( ).equals( name ) ) {
                    return kind;
                }
            }
            return Kind.UNKNOWN;
        }

    }

}
