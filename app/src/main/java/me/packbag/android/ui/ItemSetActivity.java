package me.packbag.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.raizlabs.android.dbflow.structure.BaseModel;

import org.androidannotations.annotations.EActivity;

import java.util.List;

import me.packbag.android.App;
import me.packbag.android.db.api.Dao;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.network.Backend;
import me.packbag.android.ui.utils.RxServiceFragment;
import rx.Observable;
import rx.functions.Func2;

@EActivity
public class ItemSetActivity extends AppCompatActivity {

    public static final Func2<ItemSet, ItemSet, Integer> SORT_FUNCTION = (itemSet, itemSet2) -> itemSet.getName()
            .compareTo(itemSet2.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxServiceFragment.get(this, new LoaderFragment(), "service");
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, ItemSetFragment_.builder().build(), "callback").commit();
    }

    public static class LoaderFragment extends RxServiceFragment {

        Observable<List<ItemSet>> sets;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if (sets == null) {
                Backend backend = App.get(activity).component().backend();
                Dao dao = App.get(activity).component().dao();

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
                sets = fromBackend.mergeWith(dao.itemSets().flatMap(Observable::from).toSortedList(SORT_FUNCTION))
                        .cache()
                        .takeUntil(onDestroy);
            }
        }
    }
}
