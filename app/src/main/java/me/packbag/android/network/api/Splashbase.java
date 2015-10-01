package me.packbag.android.network.api;

import me.packbag.android.network.model.SplashbaseImage;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by astra on 17.07.2015.
 */
public interface Splashbase {

    @GET("/images/search")
    Observable<SplashbaseImage.ImageList> search(@Query("query") String term);
}
