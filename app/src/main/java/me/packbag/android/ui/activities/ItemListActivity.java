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
import org.androidannotations.annotations.Trace;
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
import me.packbag.android.ui.events.ItemListChangedEvent;
import me.packbag.android.ui.events.TakenEvent;
import me.packbag.android.ui.events.UselessEvent;
import rx.Observable;
import rx.subjects.BehaviorSubject;

@EActivity(R.layout.activity_itemlist)
@OptionsMenu(R.menu.menu_item_list)
public class ItemListActivity extends AppCompatActivity implements ItemProvider {

    public static final int REQUEST_CODE_NEW_ITEM = 1;
    @ViewById TabLayout tabs;
    @ViewById ViewPager viewPager;

    @Extra  ItemSet itemSet;
    @Inject Dao     dao;

    private BehaviorSubject<List<ItemInSet>> typedItems = BehaviorSubject.create();

    @AfterViews
    void afterViews() {
        App.get(this).component().inject(this);
        setTitle(itemSet.getName());

        viewPager.setAdapter(new ItemListFragmentsAdapter(getSupportFragmentManager(), this));
        tabs.setupWithViewPager(viewPager);
        loadItems();
    }

    private void loadItems() {
        dao.itemsInSets(itemSet).subscribe(typedItems::onNext);
    }

    @Override
    @Trace
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
    public void onEvent(TakenEvent event) {
        changeTypedItemStatus(event.item, ItemStatus.TAKEN);
    }

    @SuppressWarnings("unused")
    public void onEvent(UselessEvent event) {
        changeTypedItemStatus(event.item, ItemStatus.USELESS);
    }

    private void changeTypedItemStatus(Item item, ItemStatus itemStatus) {
        typedItems.take(1)
                .flatMap(Observable::from)
                .first(input -> input.getItem().getId() == item.getId())
                .doOnNext(input1 -> input1.setStatus(itemStatus))
                .doOnNext(BaseModel::save)
                .toList()
                .subscribe(L::i, L::e, () -> Bus.post(new ItemListChangedEvent()));
    }

    @OptionsItem(R.id.action_new_item)
    void onNewItem() {
        NewItemActivity_.intent(this).itemSet(itemSet).startForResult(REQUEST_CODE_NEW_ITEM);
    }

    @OnActivityResult(REQUEST_CODE_NEW_ITEM)
    void onItemAdded() {
        L.i();
        loadItems();
    }
}
