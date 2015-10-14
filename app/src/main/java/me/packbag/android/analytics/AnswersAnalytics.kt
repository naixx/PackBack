package me.packbag.android.analytics

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ShareEvent

import me.packbag.android.db.model.ItemSet

/**
 * Created by astra on 13.10.2015.
 */
internal class AnswersAnalytics : Analytics {

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
