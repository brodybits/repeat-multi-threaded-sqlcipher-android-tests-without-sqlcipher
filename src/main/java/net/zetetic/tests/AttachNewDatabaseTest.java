package net.zetetic.tests;

import android.database.sqlite.SQLiteDatabase;

import net.zetetic.ZeteticApplication;

import java.io.File;

public class AttachNewDatabaseTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase unencryptedDatabase) {

        unencryptedDatabase.execSQL("create table t1(a,b)");
        unencryptedDatabase.execSQL("insert into t1(a,b) values(?, ?)", new Object[]{"one", "two"});

        String newKey = "foo";
        File newDatabasePath = ZeteticApplication.getInstance().getDatabasePath("normal.db");
        String attachCommand = "ATTACH DATABASE ? as encrypted KEY ?";
        String createCommand = "create table encrypted.t1(a,b)";
        String insertCommand = "insert into encrypted.t1 SELECT * from t1";
        String detachCommand = "DETACH DATABASE encrypted";
        unencryptedDatabase.execSQL(attachCommand, new Object[]{newDatabasePath.getAbsolutePath(), newKey});
        unencryptedDatabase.execSQL(createCommand);
        unencryptedDatabase.execSQL(insertCommand);
        unencryptedDatabase.execSQL(detachCommand);

        return true;
    }

    @Override
    protected void tearDown(SQLiteDatabase database) {
        File newDatabasePath = ZeteticApplication.getInstance().getDatabasePath("normal.db");
        newDatabasePath.delete();
    }

    /** TBD was never used:
    public android.database.sqlite.SQLiteDatabase createNormalDatabase(){
        File newDatabasePath = ZeteticApplication.getInstance().getDatabasePath("normal.db");
        return android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(newDatabasePath.getAbsolutePath(), null);
    }
    **/

    @Override
    public String getName() {
        return "Attach New Database Test";
    }
}
