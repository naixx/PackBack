package me.packbag.android.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.naixx.Bus;
import com.github.naixx.L;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.api.Dao;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.db.model.ItemStatus;
import me.packbag.android.ui.ItemProvider;
import me.packbag.android.ui.adapters.ItemListFragmentsAdapter;
import me.packbag.android.ui.events.ItemCount;
import me.packbag.android.ui.events.ItemStatusChangedEvent;
import rx.Observable;
import rx.observables.GroupedObservable;
import rx.subjects.BehaviorSubject;

@EActivity(R.layout.activity_itemlist)
@OptionsMenu(R.menu.activity_item_list)
public class ItemListActivity extends AppCompatActivity implements ItemProvider {

    public static final int REQUEST_CODE_NEW_ITEM = 1;
    @ViewById TabLayout tabs;
    @ViewById ViewPager viewPager;

    @Extra  ItemSet itemSet;
    @Inject Dao     dao;

    private BehaviorSubject<List<ItemInSet>> typedItems        = BehaviorSubject.create();
    private BehaviorSubject<ItemStatus>      itemStatusChanged = BehaviorSubject.create(ItemStatus.TAKEN);

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
        Observable<Map<ItemStatus, Integer>> statusCounts = itemStatusChanged.flatMap(i -> //`
                typedItems.flatMap(itemInSets -> {
                    Observable<GroupedObservable<ItemStatus, ItemInSet>> go = Observable.from(itemInSets)
                            .groupBy(ItemInSet::getStatus);
                    return go.flatMap((GroupedObservable<ItemStatus, ItemInSet> obs) -> {
                        return obs.count().map((Integer count) -> new ItemCount(obs.getKey(), count));
                    }).toMap(ItemCount::getStatus, ItemCount::getCount);
                }));
        itemStatusChanged.filter(prev -> prev == ItemStatus.CURRENT)
                .doOnNext(L::i)
                .flatMap(prev -> typedItems.take(1))
                .doOnNext(L::v)
                .map(itemInSets -> FluentIterable.from(itemInSets)
                        .filter(it -> it.getStatus() == ItemStatus.CURRENT)
                        .size())
                .filter(count -> count == 0)
                .subscribe(was -> {
                    BagPackedActivity_.intent(this).itemSet(itemSet).start();
                });

        statusCounts.subscribe((map) -> {
            L.v(map);
            adapter.onEvent(map);
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
    public Observable<List<ItemInSet>> getItems(ItemStatus itemStatus) {
        return typedItems.flatMap((List<ItemInSet> itemInSets) -> {
            return Observable.from(itemInSets)
                    .filter(input -> input.getStatus() == itemStatus)
                    .toSortedList((item, item2) -> {
                        return Ordering.natural()
                                .onResultOf((ItemInSet item1) -> item1.getItem().getCategory().getId())
                                .compare(item, item2);
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
        ItemStatus prev = e.getItem().getStatus();
        e.getItem().setStatus(e.getStatus()).async().save();
        typedItems.take(1).subscribe(typedItems::onNext);
        itemStatusChanged.onNext(prev);
    }

    @OptionsItem(R.id.action_new_item)
    void onNewItem() {
        NewItemActivity_.intent(this).itemSet(itemSet).startForResult(REQUEST_CODE_NEW_ITEM);
    }

    @OptionsItem(R.id.action_clear_items)
    void onClearItems() {
        dao.clearItems(itemSet);
        loadItems();
        scrollToFirstPage();
    }

    @OnActivityResult(REQUEST_CODE_NEW_ITEM)
    void onItemAdded() {
        L.i();
        loadItems();
        scrollToFirstPage();
    }

    private void scrollToFirstPage() {typedItems.take(1).subscribe(itemInSets -> viewPager.setCurrentItem(0));}
}
