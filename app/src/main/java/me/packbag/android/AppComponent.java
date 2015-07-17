package me.packbag.android;

import javax.inject.Singleton;

import dagger.Component;
import me.packbag.android.db.DbModule;
import me.packbag.android.network.Backend;
import me.packbag.android.network.NetworkModule;
import me.packbag.android.network.Splashable;
import me.packbag.android.ui.MainActivity;

@Singleton
@Component(
        modules = { AppModule.class, DbModule.class, NetworkModule.class })
public interface AppComponent {

    Backend backend();

    Splashable splashable();

    void inject(MainActivity mainActivity);

//	void inject(@NonNull MainActivity service);
//
//	void inject(@NonNull ProductsActivity productsActivity);
}
