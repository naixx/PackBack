package me.packbag.android.analytics

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.ShareEvent
import me.packbag.android.db.model.Item

import me.packbag.android.db.model.ItemSet
import me.packbag.android.db.model.ItemStatus

/**
 * Created by astra on 13.10.2015.
 */
internal class AnswersAnalytics : Analytics {

    override fun logAddItem(itemSet: ItemSet, item: Item) {
        Answers.getInstance().logCustom(ItemAddedEvent(itemSet, item))

    }

    override fun logItemStatusChanged(itemSet: ItemSet, item: Item, itemStatus: ItemStatus) {
        Answers.getInstance().logCustom(ItemStatusChangedEvent(itemSet, item, itemStatus))

    }

    override fun logItemSetView(itemSet: ItemSet) {
        Answers.getInstance().logContentView(ContentViewEvent()
                .putContentId(itemSet.id.toString())
                .putContentName(itemSet.name))

    }

    override fun logShare(itemSet: ItemSet, method: String) {
        Answers.getInstance().logShare(ShareEvent()
                .putMethod(method)
                .putContentId(itemSet.id.toString())
                .putContentName(itemSet.name))
    }

    override fun logShareCancelled(itemSet: ItemSet, method: String) {
        Answers.getInstance().logCustom(ShareCancelledEvent(itemSet, method))
    }
}
