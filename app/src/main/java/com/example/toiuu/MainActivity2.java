package com.example.toiuu;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private SQLiteDatabase db;
    private TextView resultView;
    private int rowCount = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        SQLiteOpenHelper helper = new SQLiteOpenHelper(this, "optimization.db", null, 2) {  // Increment to 2
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE IF NOT EXISTS TestData (id INTEGER PRIMARY KEY, value TEXT)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS TestData");
                onCreate(db);
            }
        };
        db = helper.getWritableDatabase();

        Button btnWithoutTransaction = findViewById(R.id.btnWithoutTransaction);
        Button btnWithTransaction = findViewById(R.id.btnWithTransaction);
        Button btnWithCompiledStatement = findViewById(R.id.btnWithCompiledStatement);
        Button btnBatchInsertWithCompiledStatement = findViewById(R.id.btnBatchInsertWithCompiledStatement);
        Button btnFetchAllColumns = findViewById(R.id.btnFetchAllColumns);
        Button btnFetchSpecificColumn = findViewById(R.id.btnFetchSpecificColumn);

        TextView resultWithoutTransaction = findViewById(R.id.resultWithoutTransaction);
        TextView resultWithTransaction = findViewById(R.id.resultWithTransaction);
        TextView resultWithCompiledStatement = findViewById(R.id.resultWithCompiledStatement);
        TextView resultBatchInsertWithCompiledStatement = findViewById(R.id.resultBatchInsertWithCompiledStatement);
        TextView resultFetchAllColumns = findViewById(R.id.resultFetchAllColumns);
        TextView resultFetchSpecificColumn = findViewById(R.id.resultFetchSpecificColumn);

        btnWithoutTransaction.setOnClickListener(v -> {
            long time = insertWithoutTransaction(rowCount);
            resultWithoutTransaction.setText("Insert without transaction: " + time + " ms");
        });

        btnWithTransaction.setOnClickListener(v -> {
            long time = insertWithTransaction(rowCount);
            resultWithTransaction.setText("Insert with transaction: " + time + " ms");
        });

        btnWithCompiledStatement.setOnClickListener(v -> {
            long time = insertWithCompiledStatement(rowCount);
            resultWithCompiledStatement.setText("Insert with compiled statement: " + time + " ms");
        });

        btnBatchInsertWithCompiledStatement.setOnClickListener(v -> {
            long time = batchInsertWithCompiledStatement(rowCount);
            resultBatchInsertWithCompiledStatement.setText("Batch insert with transaction and compiled statement: " + time + " ms");
        });

        btnFetchAllColumns.setOnClickListener(v -> {
            long time = fetchAllColumns();
            resultFetchAllColumns.setText("Fetch all columns: " + time + " ms");
        });

        btnFetchSpecificColumn.setOnClickListener(v -> {
            long time = fetchSpecificColumn();
            resultFetchSpecificColumn.setText("Fetch specific column (id only): " + time + " ms");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private void displayResult(String result) {
        String currentText = resultView.getText().toString();
        resultView.setText(currentText + "\n" + result);
    }

    private long insertWithoutTransaction(int count) {
        long startTime = System.currentTimeMillis();
        try {
            for (int i = 0; i < count; i++) {
                db.execSQL("INSERT INTO TestData (value) VALUES ('Sample')");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() - startTime;
    }

    private long insertWithTransaction(int count) {
        long startTime = System.currentTimeMillis();
        db.beginTransaction();
        try {
            for (int i = 0; i < count; i++) {
                db.execSQL("INSERT INTO TestData (value) VALUES ('Sample')");
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return System.currentTimeMillis() - startTime;
    }

    private long insertWithCompiledStatement(int count) {
        String sql = "INSERT INTO TestData (value) VALUES (?)";
        SQLiteStatement statement = db.compileStatement(sql);
        long startTime = System.currentTimeMillis();
        try {
            for (int i = 0; i < count; i++) {
                statement.bindString(1, "Sample");
                statement.executeInsert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() - startTime;
    }

    private long batchInsertWithCompiledStatement(int count) {
        String sql = "INSERT INTO TestData (value) VALUES (?)";
        SQLiteStatement statement = db.compileStatement(sql);
        long startTime = System.currentTimeMillis();
        db.beginTransaction();
        try {
            for (int i = 0; i < count; i++) {
                statement.bindString(1, "Sample");
                statement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return System.currentTimeMillis() - startTime;
    }

    private long fetchAllColumns() {
        long startTime = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT * FROM TestData", null);
        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String value = cursor.getString(cursor.getColumnIndex("value"));
            }
        } finally {
            cursor.close();
        }
        return System.currentTimeMillis() - startTime;
    }

    private long fetchSpecificColumn() {
        long startTime = System.currentTimeMillis();
        Cursor cursor = db.rawQuery("SELECT id FROM TestData", null);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }
        return System.currentTimeMillis() - startTime;
    }
}
