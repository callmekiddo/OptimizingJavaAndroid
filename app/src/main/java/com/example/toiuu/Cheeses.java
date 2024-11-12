package com.example.toiuu;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.SystemClock;

public class Cheeses {

    private static final String[] sBaseCheeseNames = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", /* ... */ "Vieux Boulogne"
    };

    private static final String[] sBaseCheeseOrigins = {
            "Notre-Dame de Belloc", "Mont des Cats", /* ... */ "Boulogne-sur-Mer"
    };

    private final SQLiteDatabase db;
    private String[] sCheeseNames;
    private String[] sCheeseOrigins;

    public Cheeses(int dataSize) {
        db = SQLiteDatabase.create(null);
        db.execSQL("CREATE TABLE cheese (name TEXT, origin TEXT)");
        insertData(dataSize);
    }

    private void insertData(int dataSize) {
        sCheeseNames = new String[dataSize];
        sCheeseOrigins = new String[dataSize];
        for (int i = 0; i < dataSize; i++) {
            sCheeseNames[i] = sBaseCheeseNames[i % sBaseCheeseNames.length];
            sCheeseOrigins[i] = sBaseCheeseOrigins[i % sBaseCheeseOrigins.length];
        }
    }

    public long populateWithStringPlus() {
        long startTime = SystemClock.elapsedRealtime();
        int i = 0;
        for (String name : sCheeseNames) {
            String origin = sCheeseOrigins[i++];
            String sql = "INSERT INTO cheese VALUES(\"" + name + "\",\"" + origin + "\")";
            db.execSQL(sql);
        }
        return SystemClock.elapsedRealtime() - startTime;
    }

    public long populateWithStringFormat() {
        long startTime = SystemClock.elapsedRealtime();
        int i = 0;
        for (String name : sCheeseNames) {
            String origin = sCheeseOrigins[i++];
            String sql = String.format("INSERT INTO cheese VALUES(\"%s\",\"%s\")", name, origin);
            db.execSQL(sql);
        }
        return SystemClock.elapsedRealtime() - startTime;
    }

    public long populateWithStringBuilder() {
        long startTime = SystemClock.elapsedRealtime();
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO cheese VALUES(\"");
        int resetLength = builder.length();
        int i = 0;
        for (String name : sCheeseNames) {
            String origin = sCheeseOrigins[i++];
            builder.setLength(resetLength);
            builder.append(name).append("\",\"").append(origin).append("\")");
            db.execSQL(builder.toString());
        }
        return SystemClock.elapsedRealtime() - startTime;
    }

    public long populateWithCompileStatement() {
        long startTime = SystemClock.elapsedRealtime();
        SQLiteStatement stmt = db.compileStatement("INSERT INTO cheese VALUES(?,?)");
        int i = 0;
        for (String name : sCheeseNames) {
            String origin = sCheeseOrigins[i++];
            stmt.clearBindings();
            stmt.bindString(1, name);
            stmt.bindString(2, origin);
            stmt.executeInsert();
        }
        return SystemClock.elapsedRealtime() - startTime;
    }

    public long populateWithContentValues() {
        long startTime = SystemClock.elapsedRealtime();
        ContentValues values = new ContentValues();
        int i = 0;
        for (String name : sCheeseNames) {
            String origin = sCheeseOrigins[i++];
            values.clear();
            values.put("name", name);
            values.put("origin", origin);
            db.insert("cheese", null, values);
        }
        return SystemClock.elapsedRealtime() - startTime;
    }

    public long populateWithCompileStatementOneTransaction() {
        long startTime = SystemClock.elapsedRealtime();
        db.beginTransaction();
        try {
            SQLiteStatement stmt = db.compileStatement("INSERT INTO cheese VALUES(?,?)");
            int i = 0;
            for (String name : sCheeseNames) {
                String origin = sCheeseOrigins[i++];
                stmt.clearBindings();
                stmt.bindString(1, name);
                stmt.bindString(2, origin);
                stmt.executeInsert();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return SystemClock.elapsedRealtime() - startTime;
    }

    public long iterateBothColumns() {
        long startTime = SystemClock.elapsedRealtime();
        Cursor c = db.query("cheese", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = c.getString(c.getColumnIndex("name"));
                @SuppressLint("Range") String origin = c.getString(c.getColumnIndex("origin"));
            } while (c.moveToNext());
        }
        c.close();
        return SystemClock.elapsedRealtime() - startTime;
    }

    public long iterateFirstColumn() {
        long startTime = SystemClock.elapsedRealtime();
        Cursor c = db.query("cheese", new String[]{"name"}, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = c.getString(c.getColumnIndex("name"));
            } while (c.moveToNext());
        }
        c.close();
        return SystemClock.elapsedRealtime() - startTime;
    }
}
