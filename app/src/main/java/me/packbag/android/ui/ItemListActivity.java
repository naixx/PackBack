package me.packbag.android.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemSet;

@EActivity(R.layout.activity_itemlist)
public class ItemListActivity extends AppCompatActivity implements ItemProvider {

    private static class TypedItem {

        public final Item item;
        public final Type type;

        TypedItem(Item item, Type type) {
            this.item = item;
            this.type = type;
        }
    }

    @ViewById TabLayout tabs;
    @ViewById ViewPager viewPager;

    @Extra  ItemSet         itemSet;
    private List<TypedItem> typedItems;

    @AfterViews
    void afterViews() {
        App.get(this).component().inject(this);
        setTitle(itemSet.getName());
        typedItems = FluentIterable.from(itemSet.getItems())
                .transform(item -> new TypedItem(item, Type.CURRENT))
                .toSortedList(Ordering.natural().onResultOf(input -> input.item.getId()));
        viewPager.setAdapter(new ItemListFragmentsAdapter(getSupportFragmentManager(), this));
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public List<Item> getItems(Type type) {
        return FluentIterable.from(typedItems).filter(input -> input.type == type).transform(it -> it.item).toList();
    }
}
