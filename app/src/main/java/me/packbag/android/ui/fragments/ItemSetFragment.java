package me.packbag.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github.naixx.BaseAdapter;
import com.github.naixx.L;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentByTag;
import org.androidannotations.annotations.ViewById;

import me.packbag.android.R;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.ui.activities.ItemListActivity_;
import me.packbag.android.ui.activities.ItemSetActivity;
import me.packbag.android.ui.adapters.ItemSetAdapter;

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
        service.getSets().compose(async2ui()).subscribe(adapter::swapItems, e -> {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            L.e(e);
        });
    }

    @Override
    public void onClick(ItemSet itemSet) {
        ItemListActivity_.intent(this).itemSet(itemSet).start();
    }
}
