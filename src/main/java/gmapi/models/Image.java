package gmapi.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URL;

@Data
@EqualsAndHashCode( callSuper = false )
public final class Image extends GMObject {

    private URL url;

    private String aspectRatio;
    private boolean autogen;

    private ColorStyle colorStyles;

    public Image( ) {
        super( Kind.IMAGE );
    }

}
