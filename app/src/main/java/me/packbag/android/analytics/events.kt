package me.packbag.android.analytics

import com.crashlytics.android.answers.CustomEvent
import me.packbag.android.db.model.Item
import me.packbag.android.db.model.ItemSet
import me.packbag.android.db.model.ItemStatus

internal class ShareCancelledEvent(itemSet: ItemSet, method: String) : CustomEvent("shareCancelled") {
    init {
        putCustomAttribute("Content ID", itemSet.id.toString())
        putCustomAttribute("Content Name", itemSet.name)
        putCustomAttribute("Method", method)
    }
}

internal class ItemStatusChangedEvent(itemSet: ItemSet, item: Item, itemStatus: ItemStatus) :
        CustomEvent("itemStatus${itemStatus.toString().toLowerCase().capitalize()}") {
    init {
        putCustomAttribute("Item Set", itemSet.id.toString())
        putCustomAttribute("Item Set Name", itemSet.name)
        putCustomAttribute("Item", item.serverId.toString())
        putCustomAttribute("Item Name", item.name)
        putCustomAttribute("Status", itemStatus.toString())
    }
}
