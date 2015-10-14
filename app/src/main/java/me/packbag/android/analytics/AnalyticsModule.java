package me.packbag.android.analytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {

    @Provides @Singleton
    Analytics provideAnalytics(){
        return new AnswersAnalytics();
    }
}
