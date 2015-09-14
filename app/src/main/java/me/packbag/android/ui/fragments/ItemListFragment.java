package me.packbag.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.naixx.Bus;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemStatus;
import me.packbag.android.ui.ItemProvider;
import me.packbag.android.ui.adapters.ItemsAdapter;
import me.packbag.android.ui.events.ItemListChangedEvent;

@EFragment(R.layout.fragment_itemlist)
public class ItemListFragment extends Fragment {

    @ViewById    RecyclerView recyclerView;
    @FragmentArg ItemStatus   status;
    private      ItemsAdapter adapter;

    @AfterViews
    void afterViews() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        adapter.swapItems(getItems());
    }

    private List<Item> getItems() {
        return ((ItemProvider) getContext()).getItems(status);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter();
        Bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Bus.unregister(this);
    }

    @Trace
    public void onEvent(ItemListChangedEvent event) {
        adapter.swapItems(getItems());
    }
}
