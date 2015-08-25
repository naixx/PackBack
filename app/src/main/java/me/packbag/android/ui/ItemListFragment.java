package me.packbag.android.ui;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.packbag.android.R;
import me.packbag.android.db.model.Item;

@EFragment(R.layout.fragment_itemlist)
public class ItemListFragment extends Fragment {

    @ViewById    RecyclerView      recyclerView;
    @FragmentArg ItemProvider.Type type;

    @AfterViews
    void afterViews() {
        ItemListAdapter adapter = new ItemListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        adapter.swapItems(getItems());
    }

    private List<Item> getItems() {
        return ((ItemProvider) getContext()).getItems(type);
    }
}
