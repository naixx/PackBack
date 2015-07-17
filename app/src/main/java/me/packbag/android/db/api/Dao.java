package me.packbag.android.db.api;

import java.util.List;

import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemSet;
import rx.Observable;

/**
 * Created by astra on 22.05.2015.
 */
public interface Dao {

    Observable<Item> findAllProducts();

    Observable<List<ItemSet>> itemSets();
}
