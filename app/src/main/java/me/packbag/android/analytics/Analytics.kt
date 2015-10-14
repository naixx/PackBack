package me.packbag.android.analytics

import me.packbag.android.db.model.Item
import me.packbag.android.db.model.ItemSet
import me.packbag.android.db.model.ItemStatus

/**
 * Created by astra on 13.10.2015.
 */
interface Analytics {
    fun logShare(itemSet: ItemSet, method: String)
    fun logShareCancelled(itemSet: ItemSet, method: String)
    fun logItemSetView(itemSet: ItemSet)
    fun logItemStatusChanged(itemSet: ItemSet, item: Item, itemStatus: ItemStatus)
}
