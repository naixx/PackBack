package me.packbag.android.ui.events

import me.packbag.android.db.model.Item
import me.packbag.android.db.model.ItemStatus

data class ItemStatusChangedEvent(val item: Item, val status: ItemStatus)

