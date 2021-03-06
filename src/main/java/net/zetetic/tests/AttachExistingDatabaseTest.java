package net.zetetic.tests;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteDatabaseHook;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.io.IOException;

// XXX TODO (??) - BROKEN with built-in Android database API:
public class AttachExistingDatabaseTest extends SQLCipherTest {

    @Override
    protected SQLiteDatabase createDatabase(File databasePath) {
        //SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
        //    public void preKey(SQLiteDatabase database) {}
        //    public void postKey(SQLiteDatabase database) {
        //        database.execSQL("PRAGMA cipher_default_kdf_iter = 4000;");
        //    }
        //};
        //return SQLiteDatabase.openOrCreateDatabase(databasePath,
        //        ZeteticApplication.DATABASE_PASSWORD, null, hook);
        return SQLiteDatabase.openOrCreateDatabase(databasePath, null);
    }

    @Override
    public boolean execute(SQLiteDatabase database) {

        try {
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(ZeteticApplication.ONE_X_DATABASE);
            File other = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.ONE_X_DATABASE);
            String otherPath = other.getAbsolutePath();
            String attach = String.format("attach database ? as other key ?");
            // XXX TODO:
            //database.rawExecSQL("pragma cipher_default_use_hmac = off");
            //database.rawExecSQL("pragma cipher_default_kdf_iter = 4000;");
            database.execSQL(attach, new Object[]{otherPath, ZeteticApplication.DATABASE_PASSWORD});
            Cursor result = database.rawQuery("select * from other.t1", new String[]{});
            String a = "";
            String b = "";
            if(result != null){
                result.moveToFirst();
                a = result.getString(0);
                b = result.getString(1);
                result.close();
            }
            database.execSQL("detach database other");
            // XXX TODO:
            //database.rawExecSQL("pragma cipher_default_kdf_iter = 64000;");
            return a.length() > 0 && b.length() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void tearDown(SQLiteDatabase database) {
        ZeteticApplication.getInstance().delete1xDatabase();
        database.execSQL("PRAGMA cipher_default_kdf_iter = 64000;");
    }

    @Override
    public String getName() {
        return "Attach Existing Database Test";
    }
}
