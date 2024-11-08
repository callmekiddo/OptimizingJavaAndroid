package com.example.toiuu;

import androidx.appcompat.app.AppCompatActivity;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    private SQLiteDatabase db;
    private TextView resultsTextView;
    private final int NUM_RECORDS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        resultsTextView = findViewById(R.id.results);

        Button runAllButton = findViewById(R.id.runAllInsert);
        runAllButton.setOnClickListener(v -> runAllInsert());
    }

    // Function to run all insertion tests and display results
    private void runAllInsert() {
        StringBuilder results = new StringBuilder();

        results.append("No Optimization: ")
                .append(testInsertionWithoutOptimization())
                .append(" ms\n");

        results.append("Optimized with Compiled Statement: ")
                .append(testInsertionWithCompiledStatement())
                .append(" ms\n");

        results.append("Optimized with Transaction: ")
                .append(testInsertionWithTransaction())
                .append(" ms\n");

        results.append("Optimized with InsertHelper: ")
                .append(testInsertionWithInsertHelper())
                .append(" ms\n");

        resultsTextView.setText(results.toString());
    }

    // 1. Without optimization
    private long testInsertionWithoutOptimization() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NUM_RECORDS; i++) {
            db.execSQL("INSERT INTO TestTable (name, age) VALUES ('Name " + i + "', " + (i % 100) + ")");
        }
        return System.currentTimeMillis() - startTime;
    }

    // 2. Using Compiled Statement
    private long testInsertionWithCompiledStatement() {
        long startTime = System.currentTimeMillis();
        SQLiteStatement statement = db.compileStatement("INSERT INTO TestTable (name, age) VALUES (?, ?)");
        for (int i = 0; i < NUM_RECORDS; i++) {
            statement.bindString(1, "Name " + i);
            statement.bindLong(2, i % 100);
            statement.executeInsert();
        }
        return System.currentTimeMillis() - startTime;
    }

    // 3. Using Transactions
    private long testInsertionWithTransaction() {
        long startTime = System.currentTimeMillis();
        db.beginTransaction();
        try {
            for (int i = 0; i < NUM_RECORDS; i++) {
                db.execSQL("INSERT INTO TestTable (name, age) VALUES ('Name " + i + "', " + (i % 100) + ")");
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return System.currentTimeMillis() - startTime;
    }

    // 4. Using DatabaseUtils.InsertHelper (deprecated but illustrative)
    private long testInsertionWithInsertHelper() {
        long startTime = System.currentTimeMillis();
        DatabaseUtils.InsertHelper insertHelper = new DatabaseUtils.InsertHelper(db, "TestTable");
        final int nameColumn = insertHelper.getColumnIndex("name");
        final int ageColumn = insertHelper.getColumnIndex("age");

        db.beginTransaction();
        try {
            for (int i = 0; i < NUM_RECORDS; i++) {
                insertHelper.prepareForInsert();
                insertHelper.bind(nameColumn, "Name " + i);
                insertHelper.bind(ageColumn, i % 100);
                insertHelper.execute();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            insertHelper.close();
        }
        return System.currentTimeMillis() - startTime;
    }

    // Helper class to manage the database
    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "optimization.db";
        private static final int DATABASE_VERSION = 1;

        DatabaseHelper(MainActivity2 context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS TestTable (id INTEGER PRIMARY KEY, name TEXT, age INTEGER)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS TestTable");
            onCreate(db);
        }
    }
}
