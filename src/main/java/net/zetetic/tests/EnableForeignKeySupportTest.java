package net.zetetic.tests;

import android.database.sqlite.SQLiteDatabase;

import net.zetetic.QueryHelper;

public class EnableForeignKeySupportTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        String defaultValue = QueryHelper.singleValueFromQuery(database, "PRAGMA foreign_keys");
        database.execSQL("PRAGMA foreign_keys = ON;", new Object[]{});
        String updatedValue = QueryHelper.singleValueFromQuery(database, "PRAGMA foreign_keys");
        return defaultValue.equals("0") && updatedValue.equals("1");
    }

    @Override
    public String getName() {
        return "Enable Foreign Key Support Test";
    }
}
