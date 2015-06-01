package net.zetetic.tests;

import android.util.Log;

import android.database.sqlite.SQLiteDatabase;

import net.zetetic.QueryHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.util.Date;

// XXX TODO BROKEN with built-in Android database API:
public class AverageOpenTimeTest extends SQLCipherTest {

    int MAX_ATTEMPTS = 10;

    @Override
    public boolean execute(SQLiteDatabase database) {

        database.close();
        boolean status = false;
        Float runs = 0.0f;
        File databasePath = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            Date start = new Date();
            database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
            Date end = new Date();
            String kdf = QueryHelper.singleValueFromQuery(database, "PRAGMA kdf_iter;");
            Log.i(TAG, String.format("KDF value:%s", kdf));
            database.close();
            runs += (end.getTime() - start.getTime()) / 1000.0f;
        }

        status = true;
        setMessage(String.format("%s attempts, %.3f second average open time", MAX_ATTEMPTS, runs/MAX_ATTEMPTS));
        return status;
    }

    @Override
    protected SQLiteDatabase createDatabase(File databasePath) {
        return SQLiteDatabase.openOrCreateDatabase(databasePath, null);
    }

    @Override
    public String getName() {
        return "Average Database Open Time";
    }
}
