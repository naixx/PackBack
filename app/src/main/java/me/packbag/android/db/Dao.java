package me.packbag.android.db;

import me.packbag.android.db.model.Item;
import rx.Observable;

/**
 * Created by astra on 22.05.2015.
 */
public interface Dao {

    Observable<Item> findAllProducts();
}
