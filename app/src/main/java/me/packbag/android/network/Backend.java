package me.packbag.android.network;

import java.util.List;

import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemCategory;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by astra on 12.07.2015.
 */
public interface Backend {

    @GET("/item_categories")
    Observable<List<ItemCategory>> itemCategories();

    @GET("/items")
    Observable<List<Item>> items();
}
