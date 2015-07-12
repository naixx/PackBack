package me.packbag.android.network;

import com.raizlabs.android.dbflow.annotation.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.packbag.android.BuildConfig;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * Created by astra on 12.07.2015.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    @NotNull
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder().setEndpoint("http://hikapro.com/api/v1/backpack")
                                        .setConverter(new JacksonConverter())
                                        .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                                        .build();
    }

    @Provides
    @Singleton
    @NotNull
    Backend provideBackend(RestAdapter restAdapter) {
        return restAdapter.create(Backend.class);
    }
}
