package me.packbag.android.db.api;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;

import me.packbag.android.db.model.ItemCategory;
import me.packbag.android.db.model.ItemCategory_Table;

@Migration(version = 0, databaseName = Db.NAME)
public class OnCreateMigration extends BaseMigration {

    @Override
    public void migrate(SQLiteDatabase database) {
        ContentValues cv = new ContentValues();
        cv.put(ItemCategory_Table.ID, ItemCategory.USER);
        cv.put(ItemCategory_Table.NAME, "User");
        database.insert(ItemCategory_Table.TABLE_NAME, null, cv);
    }
}
