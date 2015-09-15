package me.packbag.android.ui;

import java.util.List;

import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemStatus;
import rx.Observable;

/**
 * Created by astra on 25.08.2015.
 */
public interface ItemProvider {

    Observable<List<Item>> getItems(ItemStatus itemStatus);
}
