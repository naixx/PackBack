package me.packbag.android.db.api;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.Update;
import com.raizlabs.android.dbflow.sql.language.Where;

import java.util.List;

import javax.inject.Singleton;

import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemCategory;
import me.packbag.android.db.model.ItemCategory_Table;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemInSet_Table;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.db.model.ItemStatus;
import rx.Observable;
import rx.Single;

/**
 * Created by astra on 22.05.2015.
 */
@Singleton
public class Dao {

    public Observable<List<ItemSet>> itemSets() {
        return Observable.defer(() -> Observable.just(new Select().from(ItemSet.class).queryList()));
    }

    public Single<List<ItemInSet>> itemsInSets(ItemSet itemSet) {
        return Single.create(singleSubscriber -> {
            singleSubscriber.onSuccess(new Select().from(ItemInSet.class)
                    .where(Condition.column(ItemInSet_Table.ITEMSET_ITEM_SET_ID).eq(itemSet.getId()))
                    .queryList());
        });
    }

    public Single<List<ItemCategory>> categories(ItemSet itemSet) {
        return Single.create(singleSubscriber -> {
            singleSubscriber.onSuccess(new Select().from(ItemCategory.class)
                    .orderBy(OrderBy.columns(ItemCategory_Table.ID).ascending())
                    .queryList());
        });
    }

    public Single<List<Item>> itemsAll() {
        return Single.create(singleSubscriber -> {
            singleSubscriber.onSuccess(new Select().from(Item.class).queryList());
        });
    }

    public void clearItems(ItemSet itemSet) {
        Where where = Update.table(ItemInSet.class)
                .set(Condition.column(ItemInSet_Table.STATUS).eq(ItemStatus.CURRENT))
                .where(Condition.column(ItemInSet_Table.ITEMSET_ITEM_SET_ID).eq(itemSet.getId()));
        where.queryClose();
    }
}
