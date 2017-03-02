package gmapi.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode( callSuper = false )
public final class Color {

    private int red;
    private int green;
    private int blue;

}
