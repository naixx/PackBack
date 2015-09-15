package me.packbag.android.db.api;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import javax.inject.Singleton;

import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemInSet_Table;
import me.packbag.android.db.model.ItemSet;
import rx.Observable;

/**
 * Created by astra on 22.05.2015.
 */
@Singleton
public class Dao {

    public Observable<List<ItemSet>> itemSets() {
        return Observable.defer(() -> Observable.just(new Select().from(ItemSet.class).queryList()));
    }

    public Observable<List<ItemInSet>> itemsInSets(ItemSet itemSet) {
        return Observable.defer(() -> Observable.just(new Select().from(ItemInSet.class)
            .where(Condition.column(ItemInSet_Table.ITEMSET_ITEM_SET_ID).eq(itemSet.getId()))
            .queryList()));
    }
}
