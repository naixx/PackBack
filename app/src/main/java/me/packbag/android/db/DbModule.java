package me.packbag.android.db;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.packbag.android.db.api.Dao;

@Module
public class DbModule {

    @Provides
    @Singleton
    @NonNull
    Dao provideDao() {
        return new Dao();
    }

//    @Provides
//    @NonNull
//    @Singleton
//    PackDatabase provideDb(Context context) {
//        return new PackDatabase();
//    }

//    @Provides
//    @NonNull
//    @Singleton
//    DatabaseDao provideDBDao(PackDatabase db) {
//        return db.getDatabaseDao();
//    }
//
//    @Provides
//    @NonNull
//    @Singleton
//    Dao provideDao(DatabaseDao dao) {
//        return new DaoImpl(dao);
//    }
}
