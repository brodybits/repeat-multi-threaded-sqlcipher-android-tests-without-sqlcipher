package net.zetetic.tests;

import android.os.AsyncTask;
import android.util.Log;
import net.zetetic.ZeteticApplication;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteRunner extends AsyncTask<ResultNotifier, TestResult, Void> {

    private ResultNotifier notifier;

    @Override
    protected Void doInBackground(ResultNotifier... resultNotifiers) {
        this.notifier = resultNotifiers[0];
        runSuite();
        return null;
    }

    @Override
    protected void onProgressUpdate(TestResult... values) {
        notifier.send(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        notifier.complete();
    }

    private void runSuite(){
        for (SQLCipherTest test : getTestsToRun()) {
            try{
                Log.i(ZeteticApplication.TAG, "Running test:" + test.getName());
                publishProgress(test.run());
            } catch (Throwable e) {
                publishProgress(new TestResult(test.getName(), false));
            }
        }
    }

    private List<SQLCipherTest> getTestsToRun() {
        List<SQLCipherTest> tests = new ArrayList<SQLCipherTest>();

        // XXX test(s) BROKEN with built-in Android database API:
        //tests.add(new AttachDatabaseTest());
        tests.add(new GetTypeFromCrossProcessCursorWrapperTest());
        tests.add(new NullQueryResultTest());
        tests.add(new CrossProcessCursorQueryTest());
        tests.add(new InterprocessBlobQueryTest());
        tests.add(new LoopingQueryTest());
        tests.add(new LoopingCountQueryTest());
        tests.add(new AttachNewDatabaseTest());
        // XXX test(s) BROKEN with built-in Android database API:
        //tests.add(new AttachExistingDatabaseTest());
        tests.add(new CanThrowSQLiteExceptionTest());
        tests.add(new CompiledSQLUpdateTest());
        tests.add(new FullTextSearchTest());
        tests.add(new ReadableDatabaseTest());
        tests.add(new AutoVacuumOverReadTest());
        tests.add(new ReadableWritableAccessTest());
        tests.add(new CursorAccessTest());
        tests.add(new QueryNonEncryptedDatabaseTest());
        tests.add(new EnableForeignKeySupportTest());
        // XXX test(s) BROKEN with built-in Android database API:
        //tests.add(new AverageOpenTimeTest());
        //tests.add(new NestedTransactionsTest());
        // Does not work with built-in database API on most versions of Android:
        //tests.add(new UnicodeTest());
        tests.add(new MultiThreadReadWriteTest());
        tests.add(new RawQueryTest());
        tests.add(new OpenReadOnlyDatabaseTest());

        return tests;
    }
}
