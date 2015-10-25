package me.packbag.android.ui

import me.packbag.android.db.model.ItemInSet
import me.packbag.android.db.model.ItemStatus
import rx.Observable

/**
 * Created by astra on 25.08.2015.
 */
interface ItemProvider {

    fun getItems(itemStatus: ItemStatus): Observable<List<ItemInSet>>
}
