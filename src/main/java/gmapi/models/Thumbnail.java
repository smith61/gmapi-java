package gmapi.models;

import lombok.Data;

import java.net.URL;

@Data
public class Thumbnail {

    private URL url;

    private int width;
    private int height;

}
