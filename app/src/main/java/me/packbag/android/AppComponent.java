package me.packbag.android;

import javax.inject.Singleton;

import dagger.Component;
import me.packbag.android.db.DbModule;
import me.packbag.android.db.api.Dao;
import me.packbag.android.network.api.Backend;
import me.packbag.android.network.NetworkModule;
import me.packbag.android.network.api.Splashable;
import me.packbag.android.ui.activities.ItemListActivity;
import me.packbag.android.ui.activities.ItemSetActivity;

@Singleton
@Component(
        modules = { AppModule.class, DbModule.class, NetworkModule.class })
public interface AppComponent {

    Backend backend();

    Splashable splashable();

    Dao dao();

    void inject(ItemSetActivity itemSetActivity);

    void inject(ItemListActivity itemListActivity);

//	void inject(@NonNull MainActivity service);
//
//	void inject(@NonNull ProductsActivity productsActivity);
}
