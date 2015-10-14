package me.packbag.android;

import javax.inject.Singleton;

import dagger.Component;
import me.packbag.android.analytics.Analytics;
import me.packbag.android.analytics.AnalyticsModule;
import me.packbag.android.db.DbModule;
import me.packbag.android.db.api.Dao;
import me.packbag.android.network.NetworkModule;
import me.packbag.android.network.api.Backend;
import me.packbag.android.network.api.Splashbase;
import me.packbag.android.ui.activities.BagPackedActivity;
import me.packbag.android.ui.activities.ItemListActivity;
import me.packbag.android.ui.activities.ItemSetActivity;

@Singleton
@Component(
    modules = { AppModule.class, DbModule.class, NetworkModule.class, AnalyticsModule.class })
public interface AppComponent {

    Backend backend();

    Splashbase splashable();

    Dao dao();

    Analytics analytics();

    void inject(ItemSetActivity itemSetActivity);

    void inject(ItemListActivity itemListActivity);

    void inject(BagPackedActivity bagPackedActivity);

//	void inject(@NonNull MainActivity service);
//
//	void inject(@NonNull ProductsActivity productsActivity);
}
