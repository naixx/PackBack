package me.packbag.android.network.api;

import me.packbag.android.network.model.SplashableImage;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by astra on 17.07.2015.
 */
public interface Splashable {

    @GET("/images/search")
    Observable<SplashableImage.ImageList> search(@Query("query") String term);
}
