package me.packbag.android.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.api.Dao;
import me.packbag.android.db.model.ItemSet;

@EActivity(R.layout.activity_itemlist)
public class ItemListActivity extends AppCompatActivity {

    @ViewById RecyclerView recyclerView;
    @Inject   Dao          dao;
    @Extra    ItemSet      itemSet;

    @AfterViews
    void afterViews() {
        App.get(this).component().inject(this);

        ItemListAdapter adapter = new ItemListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.swapItems(itemSet.getItems());
    }
}
