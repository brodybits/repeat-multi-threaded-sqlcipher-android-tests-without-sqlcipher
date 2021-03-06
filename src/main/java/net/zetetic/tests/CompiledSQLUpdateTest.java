package net.zetetic.tests;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class CompiledSQLUpdateTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {

        database.execSQL("create table ut1(a text, b integer)", new Object[]{});
        database.execSQL("insert into ut1(a, b) values (?,?)", new Object[]{"s1", new Integer(100)});

        SQLiteStatement st = database.compileStatement("update ut1 set b = 101 where b = 100");
        long recs = st.executeUpdateDelete();
        return (recs == 1);
    }

    @Override
    public String getName() {
        return "Compiled SQL update test";
    }
}
