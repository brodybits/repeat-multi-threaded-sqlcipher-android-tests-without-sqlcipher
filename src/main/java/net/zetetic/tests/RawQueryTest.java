package net.zetetic.tests;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RawQueryTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        int rows = 0;
        database.execSQL("create table t1(a,b);");
        database.execSQL("insert into t1(a,b) values(?, ?);",
                new Object[]{"one for the money", "two for the show"});
        Cursor cursor = database.rawQuery("select * from t1;", null);
        if(cursor != null){
            while(cursor.moveToNext()) {
                cursor.getString(0);
                rows++;
            }
            cursor.close();
        }
        return rows > 0;
    }

    @Override
    public String getName() {
        return "Raw Query Test";
    }
}
