package me.packbag.android.ui.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import me.packbag.android.R
import me.packbag.android.db.model.ItemStatus
import me.packbag.android.ui.fragments.ItemListFragment_

private const val PAGE_COUNT = 3
private val STATUSES = arrayOf(ItemStatus.CURRENT, ItemStatus.TAKEN, ItemStatus.USELESS)

class ItemListFragmentsAdapter(fm: FragmentManager, private val context: Context) : FragmentPagerAdapter(fm) {

    private val tabTitles = arrayOf(R.string.tab_title_current, R.string.tab_title_taken, R.string.tab_title_useless)
    private var counts = emptyMap<ItemStatus, Int>()

    override fun getCount(): Int = PAGE_COUNT

    override fun getItem(position: Int): Fragment
            = ItemListFragment_.builder().status(STATUSES[position]).build()

    override fun getPageTitle(position: Int): CharSequence {
        if (counts.isEmpty()) {
            return context.getString(tabTitles[position])
        } else {
            val status = STATUSES[position]
            val count = counts[status] ?: 0
            return context.getString(tabTitles[position]) + " ($count)"
        }
    }

    fun onEvent(itemCounts: Map<ItemStatus, Int>) {
        counts = itemCounts
    }
}
