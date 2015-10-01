package me.packbag.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import net.tribe7.common.collect.FluentIterable;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.packbag.android.R;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemStatus;
import me.packbag.android.ui.ItemProvider;
import me.packbag.android.ui.adapters.Customizers;
import me.packbag.android.ui.adapters.ItemsAdapter;
import rx.Observable;
import rx.Subscription;

@EFragment(R.layout.fragment_itemlist)
@OptionsMenu
public class ItemListFragment extends Fragment {

    @ViewById    RecyclerView recyclerView;
    @FragmentArg ItemStatus   status;

    private ItemsAdapter adapter;

    @AfterViews
    void afterViews() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(adapter));
        loadAllItems();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        Observable<String> textChanges = RxSearchView.queryTextChanges(searchView)
                .map(it -> it.toString().toLowerCase().trim());
        Subscription s = Observable.combineLatest(textChanges,
                getItems(),
                (text, items) -> FluentIterable.from(items)
                        .filter((ItemInSet item) -> item.getItem().getName().toLowerCase().contains(text))
                        .toList()).subscribe(adapter::swapItems);

        searchView.setOnCloseListener(() -> {
            s.unsubscribe();
            loadAllItems();
            return false;
        });
    }

    private Observable<List<ItemInSet>> getItems() {
        return ((ItemProvider) getContext()).getItems(status);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ItemsAdapter(Customizers.get(status));
    }

    private void loadAllItems() {getItems().subscribe(adapter::swapItems);}
}
