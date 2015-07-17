package me.packbag.android.network;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raizlabs.android.dbflow.annotation.NotNull;

import javax.inject.Named;
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
    @Named("hikapro")
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder().setEndpoint("http://hikapro.com/api/v1/backpack")
                .setConverter(new JacksonConverter())
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
    }

    @Provides
    @Singleton
    @NotNull
    @Named("splash")
    RestAdapter provideRestSplashable() {
        return new RestAdapter.Builder().setEndpoint("http://www.splashbase.co/api/v1")
                .setConverter(new JacksonConverter(new ObjectMapper().configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
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
    Splashable provideSplashable(@Named("splash") RestAdapter restAdapter) {
        return restAdapter.create(Splashable.class);
    }
}
