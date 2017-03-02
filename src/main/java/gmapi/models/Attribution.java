package gmapi.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URL;

@Data
@EqualsAndHashCode( callSuper = false )
public final class Attribution extends GMObject {

    @SerializedName( "license_url" )
    private URL licenseUrl;
    @SerializedName( "license_title" )
    private String licenseTitle;

    @SerializedName( "source_title" )
    private String sourceTitle;
    @SerializedName( "source_url" )
    private URL sourceUrl;

    public Attribution( ) {
        super( Kind.ATTRIBUTION );
    }

}
