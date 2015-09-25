package me.packbag.android.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.common.collect.FluentIterable;

import java.util.Collections;
import java.util.List;

import me.packbag.android.R;
import me.packbag.android.db.model.ItemStatus;
import me.packbag.android.ui.events.ItemCount;
import me.packbag.android.ui.fragments.ItemListFragment_;

public class ItemListFragmentsAdapter extends FragmentPagerAdapter {

    static final int          PAGE_COUNT = 3;
    static final ItemStatus[] STATUSES   = new ItemStatus[]{ ItemStatus.CURRENT, ItemStatus.TAKEN, ItemStatus.USELESS };

    private Integer         tabTitles[] = new Integer[]{ R.string.tab_title_current, R.string.tab_title_taken, R.string.tab_title_useless };
    private List<ItemCount> counts      = Collections.emptyList();

    private Context context;

    public ItemListFragmentsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemListFragment_.builder().status(STATUSES[position]).build();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (counts.isEmpty()) {
            return context.getString(tabTitles[position]);
        } else {
            return context.getString(tabTitles[position]) + " (" + FluentIterable.from(counts)
                    .firstMatch(input -> input.getStatus() == STATUSES[position])
                    .transform(ItemCount::getCount)
                    .or(0) + ")";
        }
    }

    public void onEvent(List<ItemCount> itemCounts) {
        counts = itemCounts;
    }
}
