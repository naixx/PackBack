package me.packbag.android.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.naixx.Bus;
import com.github.naixx.L;
import com.google.common.collect.Ordering;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.api.Dao;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.db.model.ItemStatus;
import me.packbag.android.ui.ItemProvider;
import me.packbag.android.ui.adapters.ItemListFragmentsAdapter;
import me.packbag.android.ui.events.ItemCount;
import me.packbag.android.ui.events.ItemListChangedEvent;
import me.packbag.android.ui.events.ItemStatusChangedEvent;
import rx.Observable;
import rx.observables.GroupedObservable;
import rx.subjects.BehaviorSubject;

@EActivity(R.layout.activity_itemlist)
@OptionsMenu(R.menu.menu_item_list)
public class ItemListActivity extends AppCompatActivity implements ItemProvider {

    public static final int REQUEST_CODE_NEW_ITEM = 1;
    @ViewById TabLayout tabs;
    @ViewById ViewPager viewPager;

    @Extra  ItemSet itemSet;
    @Inject Dao     dao;

    private BehaviorSubject<List<ItemInSet>> typedItems        = BehaviorSubject.create();
    private BehaviorSubject<Object>          itemStatusChanged = BehaviorSubject.create(0);

    @AfterViews
    void afterViews() {
        App.get(this).component().inject(this);
        setTitle(itemSet.getName());

        ItemListFragmentsAdapter adapter = new ItemListFragmentsAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        countStatusedItemsForTitles(adapter);
        loadItems();
    }

    private void countStatusedItemsForTitles(ItemListFragmentsAdapter adapter) {
        itemStatusChanged.flatMap(i -> //
                typedItems.flatMap(itemInSets -> {
                    Observable<GroupedObservable<ItemStatus, ItemInSet>> go = Observable.from(itemInSets)
                            .groupBy(itemInSet -> itemInSet.getStatus());
                    return go.flatMap((GroupedObservable<ItemStatus, ItemInSet> obs) -> {
                        return obs.count().map((Integer count) -> new ItemCount(obs.getKey(), count));
                    }).toList();
                })).subscribe((List<ItemCount> pairs) -> {
            L.v(pairs);
            adapter.onEvent(pairs);
            updateTabTitles(adapter);
        }, L::e, L::i);
    }

    private void updateTabTitles(ItemListFragmentsAdapter adapter) {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            //noinspection ConstantConditions
            tabs.getTabAt(i).setText(adapter.getPageTitle(i));
        }
    }

    private void loadItems() {
        dao.itemsInSets(itemSet).subscribe(typedItems::onNext);
    }

    @Override
    public Observable<List<Item>> getItems(ItemStatus itemStatus) {
        return typedItems.flatMap((List<ItemInSet> itemInSets) -> {
            return Observable.from(itemInSets)
                    .filter(input -> input.getStatus() == itemStatus)
                    .map(ItemInSet::getItem)
                    .toSortedList((item, item2) -> {
                        return Ordering.natural().onResultOf((Item item1) -> item1.getCategory().getId()).compare(item, item2);
                    });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Bus.unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(ItemStatusChangedEvent e) {
        typedItems.take(1)
                .flatMap(Observable::from)
                .first(input -> input.getItem().getId() == e.getItem().getId())
                .doOnNext(input1 -> input1.setStatus(e.getStatus()))
                .doOnNext(BaseModel::save)
                .toList()
                .subscribe(L::i, L::e, () -> {
                    itemStatusChanged.onNext(0);
                    Bus.post(new ItemListChangedEvent());
                });
    }

    @OptionsItem(R.id.action_new_item)
    void onNewItem() {
        NewItemActivity_.intent(this).itemSet(itemSet).startForResult(REQUEST_CODE_NEW_ITEM);
    }

    @OptionsItem(R.id.action_clear_items)
    void onClearItems() {
        dao.clearItems(itemSet);
        loadItems();
        typedItems.take(1).subscribe(itemInSets -> viewPager.setCurrentItem(0));
    }

    @OnActivityResult(REQUEST_CODE_NEW_ITEM)
    void onItemAdded() {
        L.i();
        loadItems();
    }
}
