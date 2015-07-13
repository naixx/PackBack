package me.packbag.android.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.raizlabs.android.dbflow.structure.BaseModel;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.network.Backend;
import me.packbag.android.util.timber.L;
import rx.Observable;

import static me.packbag.android.util.Rx.async2ui;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Backend backend = App.get(this).component().backend();
        backend.itemCategories()
               .compose(async2ui())
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
               .subscribe(itemSet -> {
                   L.v(itemSet.getItems());
               });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
