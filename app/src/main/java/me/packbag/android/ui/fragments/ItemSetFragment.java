package me.packbag.android.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.github.naixx.BindingBaseAdapter;
import com.github.naixx.L;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentByTag;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.packbag.android.R;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.ui.activities.ItemListActivity_;
import me.packbag.android.ui.activities.ItemSetActivity;
import me.packbag.android.ui.adapters.ItemSetAdapter;

import static com.github.naixx.Rx.async2ui;

@EFragment
public class ItemSetFragment extends ProgressFragment implements BindingBaseAdapter.InteractionListener<ItemSet> {

    @Bind(R.id.recyclerView) RecyclerView                   recyclerView;
    @FragmentByTag           ItemSetActivity.LoaderFragment service;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.fragment_itemset);
        ButterKnife.bind(this, getView());
        setContentShown(false);
        setEmptyText("Не могу загрузить списки");
        long start = System.currentTimeMillis();

        ItemSetAdapter adapter = new ItemSetAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        service.getSets().compose(async2ui()).finallyDo(() -> setContentShown(true)).subscribe((items) -> {
            L.e("end = " + (System.currentTimeMillis() - start));
            adapter.swapItems(items);
            setContentShown(items.size() > 0);
        }, e -> {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            setContentEmpty(true);
            L.e(e);
        });
    }

    @Override
    public void onClick(ItemSet itemSet) {
        ItemListActivity_.intent(this).itemSet(itemSet).start();
    }
}
