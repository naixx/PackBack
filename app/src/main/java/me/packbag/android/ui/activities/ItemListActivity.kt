package me.packbag.android.ui.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.github.naixx.Bus
import com.github.naixx.L
import me.packbag.android.App
import me.packbag.android.R
import me.packbag.android.analytics.Analytics
import me.packbag.android.db.api.Dao
import me.packbag.android.db.model.ItemInSet
import me.packbag.android.db.model.ItemSet
import me.packbag.android.db.model.ItemStatus
import me.packbag.android.ui.ItemProvider
import me.packbag.android.ui.adapters.ItemListFragmentsAdapter
import me.packbag.android.ui.events.ItemCount
import me.packbag.android.ui.events.ItemStatusChangedEvent
import org.androidannotations.annotations.*
import rx.Observable
import rx.subjects.BehaviorSubject
import javax.inject.Inject

@EActivity(R.layout.activity_itemlist)
@OptionsMenu(R.menu.activity_item_list)
open class ItemListActivity : AppCompatActivity(), ItemProvider {
    companion object {
        const val REQUEST_CODE_NEW_ITEM = 1
    }

    @ViewById lateinit var tabs: TabLayout
    @ViewById lateinit var viewPager: ViewPager

    @Extra lateinit var itemSet: ItemSet
    @Inject lateinit var dao: Dao
    @Inject lateinit var analytics: Analytics

    private val typedItems = BehaviorSubject.create<List<ItemInSet>>()
    private val itemStatusChanged = BehaviorSubject.create(ItemStatus.TAKEN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.get(this).component().inject(this)
        if (savedInstanceState == null) {
            analytics.logItemSetView(itemSet)
        }
    }

    @AfterViews
    fun afterViews() {
        title = itemSet.name

        val adapter = ItemListFragmentsAdapter(supportFragmentManager, this)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        countStatusedItemsForTitles(adapter)
        loadItems()
    }

    private fun countStatusedItemsForTitles(adapter: ItemListFragmentsAdapter) {
        val statusCounts = itemStatusChanged.flatMap { i ->
            typedItems.flatMap { itemInSets ->
                Observable.from(itemInSets)
                        .groupBy { it.status }
                        .flatMap { obs -> obs.count().map { count -> ItemCount(obs.key, count) } }
                        .toMap({ it.status }, { it.count })
            }
        }
        itemStatusChanged
                .filter { it == ItemStatus.CURRENT }
                .flatMap { typedItems.take(1) }
                .map { itemInSets -> itemInSets.filter { it.status == ItemStatus.CURRENT }.size }
                .filter { count -> count === 0 }
                .subscribe { BagPackedActivity_.intent(this).itemSet(itemSet).start() }

        statusCounts.subscribe({ map: Map<ItemStatus, Int> ->
            adapter.onEvent(map)
            updateTabTitles(adapter)
        }, { L.e(it) }, { L.i() })
    }

    private fun updateTabTitles(adapter: ItemListFragmentsAdapter) {
        for (i in 0..tabs.tabCount - 1) {
            //noinspection ConstantConditions
            tabs.getTabAt(i)!!.setText(adapter.getPageTitle(i))
        }
    }

    private fun loadItems() {
        dao.itemsInSets(itemSet).subscribe({ typedItems.onNext(it) })
    }

    override fun getItems(itemStatus: ItemStatus): Observable<List<ItemInSet>> {
        return typedItems.flatMap { itemInSets: List<ItemInSet> ->
            Observable.from(itemInSets)
                    .filter { input -> input.status == itemStatus }
                    .toSortedList { itemInSet1, itemInSet2 -> compareValuesBy(itemInSet1, itemInSet2, { it.item.category.id }) }
        }
    }

    override fun onStart() {
        super.onStart()
        Bus.register(this)
    }

    override fun onStop() {
        super.onStop()
        Bus.unregister(this)
    }

    fun onEvent(e: ItemStatusChangedEvent) {
        val prev = e.itemInSet.status
        e.itemInSet.setStatus(e.status).async().save()
        typedItems.take(1).subscribe { typedItems.onNext(it) }
        itemStatusChanged.onNext(prev)
        analytics.logItemStatusChanged(itemSet, e.itemInSet.item, e.status)
    }

    @OptionsItem(R.id.action_new_item)
    fun onNewItem() {
        NewItemActivity_.intent(this).itemSet(itemSet).startForResult(REQUEST_CODE_NEW_ITEM)
    }

    @OptionsItem(R.id.action_clear_items)
    fun onClearItems() {
        dao.clearItems(itemSet)
        loadItems()
        scrollToFirstPage()
    }

    @OnActivityResult(REQUEST_CODE_NEW_ITEM)
    fun onItemAdded() {
        L.i()
        loadItems()
        scrollToFirstPage()
    }

    private fun scrollToFirstPage() {
        typedItems.take(1).subscribe { itemInSets -> viewPager.currentItem = 0 }
    }


}
