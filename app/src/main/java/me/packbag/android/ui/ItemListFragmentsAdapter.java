package me.packbag.android.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.packbag.android.R;

public class ItemListFragmentsAdapter extends FragmentPagerAdapter {

    static final int     PAGE_COUNT  = 3;
    private      Integer tabTitles[] = new Integer[]{ R.string.tab_title_current, R.string.tab_title_taken, R.string.tab_title_useless };
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
        if (position == 0) {
            return ItemListFragment_.builder().type(ItemProvider.Type.CURRENT).build();
        } else if (position == 1) {
            return ItemListFragment_.builder().type(ItemProvider.Type.TAKEN).build();
        } else if (position == 2) {
            return ItemListFragment_.builder().type(ItemProvider.Type.USELESS).build();
        } else {
            throw new IllegalArgumentException("Wrong position " + position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(tabTitles[position]);
    }
}
