package me.packbag.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.naixx.BaseAdapter;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentByTag;
import org.androidannotations.annotations.ViewById;

import me.packbag.android.R;
import me.packbag.android.db.model.ItemSet;

import static com.github.naixx.Rx.async2ui;

@EFragment(R.layout.activity_itemset)
public class ItemSetFragment extends Fragment implements BaseAdapter.InteractionListener<ItemSet> {

    @ViewById      RecyclerView                   recyclerView;
    @FragmentByTag ItemSetActivity.LoaderFragment service;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ItemSetAdapter adapter = new ItemSetAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        service.sets.compose(async2ui()).subscribe(adapter::swapItems);
    }

    @Override
    public void onClick(ItemSet itemSet) {
        ItemListActivity_.intent(this).itemSet(itemSet).start();
    }
}
