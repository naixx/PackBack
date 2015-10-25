package me.packbag.android.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import me.packbag.android.R
import me.packbag.android.db.model.ItemInSet
import me.packbag.android.db.model.ItemStatus
import me.packbag.android.ui.ItemProvider
import me.packbag.android.ui.adapters.Customizers
import me.packbag.android.ui.adapters.ItemsAdapter
import org.androidannotations.annotations.*
import rx.Observable

@EFragment(R.layout.fragment_itemlist)
@OptionsMenu(R.menu.fragment_list_item)
open class ItemListFragment : Fragment() {

    @ViewById lateinit var recyclerView: RecyclerView
    @FragmentArg lateinit var status: ItemStatus

    private val adapter: ItemsAdapter by lazy { ItemsAdapter(Customizers.create(status)) }


    //TODO wait for kotlin fix
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        afterViews()
    }

    @AfterViews
    fun afterViews() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(StickyRecyclerHeadersDecoration(adapter))
        loadAllItems()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val searchView = MenuItemCompat.getActionView(menu!!.findItem(R.id.action_search)) as SearchView

        val textChanges = RxSearchView.queryTextChanges(searchView).map { it.toString().trim() }
        val s = Observable
                .combineLatest(textChanges, items) { text, items -> items.filter { it.item.name.contains(text, true) } }
                .subscribe { adapter.swapItems(it) }

        searchView.setOnCloseListener {
            s.unsubscribe()
            loadAllItems()
            false
        }
    }

    private val items: Observable<List<ItemInSet>>
        get() = (context as ItemProvider).getItems(status)

    private fun loadAllItems() {
        items.subscribe { adapter.swapItems(it) }
    }
}
