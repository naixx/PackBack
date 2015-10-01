package me.packbag.android.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.github.naixx.L;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import me.packbag.android.App;
import me.packbag.android.db.api.Dao;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemCategory;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemInSet_Table;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.db.model.Item_Table;
import me.packbag.android.network.api.Backend;
import me.packbag.android.ui.fragments.ItemSetFragment_;
import me.packbag.android.ui.utils.RxServiceFragment;
import rx.Observable;
import rx.functions.Func2;

@EActivity
public class ItemSetActivity extends AppCompatActivity {

    public static final  Func2<ItemSet, ItemSet, Integer> SORT_FUNCTION = (itemSet, itemSet2) -> itemSet.getName()
            .compareTo(itemSet2.getName());
    private static final String                           ITEM_SETS_TAG = "itemSets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxServiceFragment.get(this, new LoaderFragment(), "service");

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ITEM_SETS_TAG);
        if (fragment == null) {
            fragment = ItemSetFragment_.builder().build();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment, ITEM_SETS_TAG).commit();
        }
    }

    @AfterViews
    void afterViews() {
    }

    public static class LoaderFragment extends RxServiceFragment {

        Observable<List<ItemSet>> sets;

        public Observable<List<ItemSet>> getSets() {
            return sets;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (sets == null) {
                Backend backend = App.get(context).component().backend();
                Dao dao = App.get(context).component().dao();

                Observable<List<ItemSet>> fromBackend = backend.itemCategories()
                        .flatMap(Observable::from)
                        .doOnNext(ItemCategory::save).toList() //TODO use flatMap to cache items
                        .flatMap(itemCategories -> backend.items())
                        .flatMap(Observable::from)
                        .doOnNext(serverItem -> {
                            Item localItem = new Select().from(Item.class)
                                    .where(Condition.column(Item_Table.SERVERID).eq(serverItem.getServerId()))
                                    .querySingle();
                            if (localItem != null) {
                                localItem.setName(serverItem.getName());
                                localItem.setCategory(serverItem.getCategory().getId());
                                localItem.update();
                            } else {
                                serverItem.insert();
                            }
                        })
                        .toList()
                        .flatMap(items -> backend.sets())
                        .flatMap(Observable::from)
                        .doOnNext(BaseModel::save)
                        .doOnNext((ItemSet itemSet) -> {
                            for (Long serverId : itemSet.getServerIds()) {
                                Item item = new Select().from(Item.class)
                                        .where(Condition.column(Item_Table.SERVERID).eq(serverId))
                                        .querySingle();
                                ItemInSet itemInSet = new ItemInSet();
                                itemInSet.setItem(item.getId());
                                itemInSet.setItemSet(itemSet.getId());

                                //Where<ItemInSet> where = new Select().from(ItemInSet.class).where();

                                ItemInSet is = new Select().from(ItemInSet.class)
                                        .where(Condition.column(ItemInSet_Table.ITEM_ITEM_ID).eq(item.getId()))
                                        .and(Condition.column(ItemInSet_Table.ITEMSET_ITEM_SET_ID).eq(itemSet.getId()))
                                        .querySingle();

                                //FIXME
                                if (is == null) {
                                    itemInSet.save();
                                }
                            }
                        })
                        .toSortedList(SORT_FUNCTION)
                        .doOnError(L::e)
                        .onErrorResumeNext(Observable.<List<ItemSet>>empty());
                sets = fromBackend.mergeWith(dao.itemSets().flatMap(Observable::from).toSortedList(SORT_FUNCTION))
                        .cache()
                        .takeUntil(onDestroy);
            }
        }
    }
}
