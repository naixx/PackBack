package me.packbag.android.analytics

import com.crashlytics.android.answers.CustomEvent
import me.packbag.android.db.model.ItemSet

internal class ShareCancelledEvent(itemSet: ItemSet, method: String) : CustomEvent("shareCancelled") {
    init {
        putCustomAttribute("Content ID", itemSet.id)
        putCustomAttribute("Content Name", itemSet.name)
        putCustomAttribute("Method", method)
    }
}

