package me.packbag.android.ui;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.raizlabs.android.dbflow.structure.BaseModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.api.Dao;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.network.Backend;
import me.packbag.android.ui.utils.BaseAdapter;
import rx.Observable;
import rx.functions.Func2;

import static me.packbag.android.util.Rx.async2ui;

@EActivity(R.layout.activity_itemset)
public class ItemSetActivity extends AppCompatActivity implements BaseAdapter.InteractionListener<ItemSet> {

    public static final Func2<ItemSet, ItemSet, Integer> SORT_FUNCTION = (itemSet, itemSet2) -> itemSet.getName()
            .compareTo(itemSet2.getName());

    @ViewById RecyclerView recyclerView;
    @Inject   Dao          dao;
    @Inject   Backend      backend;

    @AfterViews
    void afterViews() {
        App.get(this).component().inject(this);

        ItemSetAdapter adapter = new ItemSetAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Observable<List<ItemSet>> fromBackend = backend.itemCategories()
                .flatMap(Observable::from)
                .doOnNext(BaseModel::save)
                .toList()
                .flatMap(itemCategories -> backend.items())
                .flatMap(Observable::from)
                .doOnNext(Item::save)
                .toList()
                .flatMap(items -> backend.sets())
                .flatMap(Observable::from)
                .doOnNext(ItemSet::save)
                .toSortedList(SORT_FUNCTION);
        fromBackend.mergeWith(dao.itemSets().flatMap(Observable::from).toSortedList(SORT_FUNCTION))
                .compose(async2ui())
                .subscribe(adapter::swapItems);
    }

    @Override
    public void onClick(ItemSet itemSet) {
        ItemListActivity_.intent(this).itemSet(itemSet).start();
    }
}
