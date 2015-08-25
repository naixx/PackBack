package me.packbag.android.ui;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.naixx.Bus;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.ui.events.ItemListChangedEvent;

@EFragment(R.layout.fragment_itemlist)
public class ItemListFragment extends Fragment {

    @ViewById    RecyclerView      recyclerView;
    @FragmentArg ItemProvider.Type type;
    private      ItemListAdapter   adapter;

    @AfterViews
    void afterViews() {
        adapter = new ItemListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        adapter.swapItems(getItems());
    }

    private List<Item> getItems() {
        return ((ItemProvider) getContext()).getItems(type);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Bus.unregister(this);
    }

    public void onEvent(ItemListChangedEvent event) {
        adapter.swapItems(getItems());
    }
}
