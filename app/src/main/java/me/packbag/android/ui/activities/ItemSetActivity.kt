package me.packbag.android.ui.activities

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.github.naixx.L
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import me.packbag.android.App
import me.packbag.android.db.api.select
import me.packbag.android.db.model.*
import me.packbag.android.ui.fragments.ItemSetFragment_
import me.packbag.android.ui.utils.RxServiceFragment
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import rx.Observable

@EActivity
open class ItemSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxServiceFragment.get(this, LoaderFragment(), "service")

        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(ITEM_SETS_TAG)
        if (fragment == null) {
            fragment = ItemSetFragment_.builder().build()
            supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, ITEM_SETS_TAG).commit()
        }
    }

    @AfterViews
    internal fun afterViews() {
    }

    class LoaderFragment : RxServiceFragment() {

        var sets: Observable<List<ItemSet>>? = null
            internal set

        override fun onAttach(context: Context) {
            super.onAttach(context)
            if (sets == null) {
                val backend = App.get(context).component().backend()
                val dao = App.get(context).component().dao()

                val fromBackend = backend.itemCategories()
                        .flatMap { Observable.from(it) }
                        .doOnNext { it.save() }
                        .toList()
                        .flatMap { itemCategories -> backend.items() }
                        .flatMap { Observable.from(it) }
                        .doOnNext { serverItem ->
                            val localItem = select<Item>().where(Condition.column(Item_Table.SERVERID).eq(serverItem.serverId)).querySingle()
                            if (localItem != null) {
                                localItem.name = serverItem.name
                                localItem.setCategory(serverItem.category.id)
                                localItem.update()
                            } else {
                                serverItem.insert()
                            }
                        }
                        .toList()
                        .flatMap { items -> backend.sets() }
                        .flatMap { Observable.from(it) }
                        .doOnNext { it.save() }
                        .doOnNext { itemSet: ItemSet ->
                            for (serverId in itemSet.serverIds) {
                                val item = select<Item>().where(Condition.column(Item_Table.SERVERID).eq(serverId)).querySingle()
                                val itemInSet = ItemInSet()
                                itemInSet.setItem(item.id)
                                itemInSet.setItemSet(itemSet.id)

                                //Where<ItemInSet> where = new Select().from(ItemInSet.class).where();

                                val `is` = Select().from(ItemInSet::class.java).where(Condition.column(ItemInSet_Table.ITEM_ITEM_ID).eq(item.id)).and(Condition.column(ItemInSet_Table.ITEMSET_ITEM_SET_ID).eq(itemSet.id)).querySingle()

                                //FIXME
                                if (`is` == null) {
                                    itemInSet.save()
                                }
                            }
                        }.toSortedList(SORT_FUNCTION).doOnError(L::e).onErrorResumeNext(Observable.empty<List<ItemSet>>())
                sets = fromBackend.mergeWith(dao.itemSets().flatMap { Observable.from(it) }.toSortedList(SORT_FUNCTION)).cache().takeUntil(onDestroy)
            }
        }
    }

    companion object {

        val SORT_FUNCTION = { itemSet: ItemSet, itemSet2: ItemSet -> itemSet.getName().compareTo(itemSet2.getName()) }
        private val ITEM_SETS_TAG = "itemSets"
    }
}
