package gmapi.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = false )
public final class ColorStyle {

    private Color primary;
    private Color scrim;
    private Color accent;

}
