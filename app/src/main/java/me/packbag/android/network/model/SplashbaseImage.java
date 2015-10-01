package me.packbag.android.network.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class SplashbaseImage {

    public static class ImageList {

        public List<SplashbaseImage> images;
    }

    public int    id;
    public String url;
    public String large_url;
}
