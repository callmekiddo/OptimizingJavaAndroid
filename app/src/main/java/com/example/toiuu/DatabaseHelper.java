package com.example.toiuu;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "items";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_VALUE = "value";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_VALUE + " INTEGER);";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // No optimized
    public void insertWithoutOptimization(ContentValues[] valuesArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            for (ContentValues values : valuesArray) {
                db.insert(TABLE_NAME, null, values);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Optimized with transaction
    public void insertWithTransaction(ContentValues[] valuesArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues values : valuesArray) {
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // Optimized with InsertHelper
    public void insertWithInsertHelper(ContentValues[] valuesArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        DatabaseUtils.InsertHelper insertHelper = new DatabaseUtils.InsertHelper(db, TABLE_NAME);
        final int nameColumn = insertHelper.getColumnIndex(COLUMN_NAME);
        final int valueColumn = insertHelper.getColumnIndex(COLUMN_VALUE);

        db.beginTransaction();
        try {
            for (ContentValues values : valuesArray) {
                insertHelper.prepareForInsert();
                insertHelper.bind(nameColumn, values.getAsString(COLUMN_NAME));
                insertHelper.bind(valueColumn, values.getAsInteger(COLUMN_VALUE));
                insertHelper.execute();
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            insertHelper.close();
        }
    }
}
