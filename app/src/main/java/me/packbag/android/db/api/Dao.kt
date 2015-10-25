package me.packbag.android.db.api

import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.OrderBy
import com.raizlabs.android.dbflow.sql.language.Select
import com.raizlabs.android.dbflow.sql.language.Update
import me.packbag.android.db.model.*
import me.packbag.android.util.flatten
import me.packbag.android.util.just
import rx.Observable
import rx.Single
import javax.inject.Singleton

/**
 * Created by astra on 22.05.2015.
 */
@Singleton
class Dao {

    fun itemSets(): Observable<List<ItemSet>> = Observable.defer {
        Select().from(ItemSet::class.java).queryList().just()
    }

    fun itemsInSets(itemSet: ItemSet): Single<List<ItemInSet>> = Single.create { singleSubscriber ->
        singleSubscriber.onSuccess(
                Select().from(ItemInSet::class.java)
                        .where(Condition.column(ItemInSet_Table.ITEMSET_ITEM_SET_ID).eq(itemSet.id))
                        .queryList())
    }

    //TODO itemSet unused?
    fun categories(itemSet: ItemSet): Single<List<ItemCategory>> = Single.create { singleSubscriber ->
        singleSubscriber.onSuccess(
                Select().from(ItemCategory::class.java)
                        .orderBy(OrderBy.columns(ItemCategory_Table.ID).ascending())
                        .queryList())
    }

    fun itemsExcludingItemSet(itemSet: ItemSet): Observable<Item> = itemsInSets(itemSet)
            .flatten()
            .map { it.item }
            .toList()
            .flatMap { itemInSets -> itemsAll().flatten().filter { item -> item !in itemInSets } }

    fun itemsAll(): Single<List<Item>> = Single.create { singleSubscriber ->
        singleSubscriber.onSuccess(Select().from(Item::class.java).queryList())
    }

    fun clearItems(itemSet: ItemSet) {
        val where = Update.table(ItemInSet::class.java)
                .set(Condition.column(ItemInSet_Table.STATUS).eq(ItemStatus.CURRENT))
                .where(Condition.column(ItemInSet_Table.ITEMSET_ITEM_SET_ID).eq(itemSet.id))
        where.queryClose()
    }
}
