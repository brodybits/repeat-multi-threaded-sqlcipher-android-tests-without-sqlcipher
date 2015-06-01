package net.zetetic.tests;

import android.database.sqlite.SQLiteDatabase;

import net.zetetic.QueryHelper;

// XXX TODO BROKEN with built-in Android database API:
public class NestedTransactionsTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("savepoint foo;", new Object[]{});
        database.execSQL("create table t1(a,b);", new Object[]{});
        database.execSQL("insert into t1(a,b) values(?,?);", new Object[]{"one for the money", "two for the show"});
        database.execSQL("savepoint bar;", new Object[]{});
        database.execSQL("insert into t1(a,b) values(?,?);", new Object[]{"three to get ready", "go man go"});
        database.execSQL("rollback transaction to bar;", new Object[]{});
        database.execSQL("commit;", new Object[]{});
        int count = QueryHelper.singleIntegerValueFromQuery(database, "select count(*) from t1;");
        return count == 1;
    }

    @Override
    public String getName() {
        return "Nested Transactions Test";
    }
}
