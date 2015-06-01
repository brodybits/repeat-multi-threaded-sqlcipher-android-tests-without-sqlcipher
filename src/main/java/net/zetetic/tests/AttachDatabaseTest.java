package net.zetetic.tests;

import android.database.sqlite.SQLiteDatabase;

import net.zetetic.QueryHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;

// XXX TODO BROKEN with built-in Android database API:
public class AttachDatabaseTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        // XXX TODO without password:
        boolean status;
        String password = "test123";
        File fooDatabase = ZeteticApplication.getInstance().getDatabasePath("foo.db");
        database.execSQL("ATTACH database ? AS encrypted KEY ?", new Object[]{fooDatabase.getAbsolutePath(), password});
        database.execSQL("create table encrypted.t1(a,b);");
        database.execSQL("insert into encrypted.t1(a,b) values(?,?);", new Object[]{"one for the money", "two for the show"});
        int rowCount = QueryHelper.singleIntegerValueFromQuery(database, "select count(*) from encrypted.t1;");
        status = rowCount == 1;
        database.close();
        return status;
    }

    @Override
    public String getName() {
        return "Attach database test";
    }
}
