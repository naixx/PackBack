package me.packbag.android.network;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raizlabs.android.dbflow.annotation.NotNull;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.packbag.android.BuildConfig;
import me.packbag.android.network.api.Backend;
import me.packbag.android.network.api.Splashbase;
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
    @Named("hikapro")
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder().setEndpoint("http://hikapro.com/api/v1/backpack")
                .setConverter(new JacksonConverter())
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE)
                .build();
    }

    @Provides
    @Singleton
    @NotNull
    @Named("splash")
    RestAdapter provideRestSplashable() {
        return new RestAdapter.Builder().setEndpoint("http://www.splashbase.co/api/v1")
                .setConverter(new JacksonConverter( //
                        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.BASIC : RestAdapter.LogLevel.NONE)
                .build();
    }

    @Provides
    @Singleton
    @NotNull
    Backend provideBackend(@Named("hikapro") RestAdapter restAdapter) {
        return restAdapter.create(Backend.class);
    }

    @Provides
    @Singleton
    @NotNull
    Splashbase provideSplashable(@Named("splash") RestAdapter restAdapter) {
        return restAdapter.create(Splashbase.class);
    }
}
