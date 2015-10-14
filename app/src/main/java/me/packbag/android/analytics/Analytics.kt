package me.packbag.android.analytics

import me.packbag.android.db.model.ItemSet

/**
 * Created by astra on 13.10.2015.
 */
interface Analytics {
    fun logShare(itemSet: ItemSet, method: String)
    fun logShareCancelled(itemSet: ItemSet, method: String)
}
