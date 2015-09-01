package me.packbag.android.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.naixx.Bus;
import com.google.common.collect.Ordering;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemInSet_Table;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.db.model.ItemStatus;
import me.packbag.android.ui.events.ItemListChangedEvent;
import me.packbag.android.ui.events.TakenEvent;
import me.packbag.android.ui.events.UselessEvent;

import static com.google.common.collect.FluentIterable.from;

@EActivity(R.layout.activity_itemlist)
public class ItemListActivity extends AppCompatActivity implements ItemProvider {

    @ViewById TabLayout tabs;
    @ViewById ViewPager viewPager;

    @Extra  ItemSet         itemSet;
    private List<ItemInSet> typedItems;

    @AfterViews
    void afterViews() {
        App.get(this).component().inject(this);
        setTitle(itemSet.getName());

        List<ItemInSet> itemInSets = new Select().from(ItemInSet.class)
                .where(Condition.column(ItemInSet_Table.ITEMSET_ITEM_SET_ID).eq(itemSet.getId()))
                .queryList();
        typedItems = from(itemInSets).toSortedList(Ordering.natural().onResultOf(ItemInSet::getId));
        viewPager.setAdapter(new ItemListFragmentsAdapter(getSupportFragmentManager(), this));
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public List<Item> getItems(ItemStatus itemStatus) {
        return from(typedItems).filter(input -> input.getStatus() == itemStatus).transform(ItemInSet::getItem).toList();
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

    public void onEvent(TakenEvent event) {
        from(typedItems).firstMatch(input -> input.getItem().equals(event.item))
                .transform(input1 -> input1.setStatus(ItemStatus.TAKEN))
                .transform(it -> {
                    it.async().save();
                    return 0;
                });
        Bus.post(new ItemListChangedEvent());
    }

    public void onEvent(UselessEvent event) {
        from(typedItems).firstMatch(input -> input.getItem().equals(event.item))
                .transform(input1 -> input1.setStatus(ItemStatus.USELESS))
                .transform(it -> {
                    it.async().save();
                    return 0;
                });
        Bus.post(new ItemListChangedEvent());
    }
}
