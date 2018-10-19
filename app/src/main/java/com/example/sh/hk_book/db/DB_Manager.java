package com.example.sh.hk_book.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sh.hk_book.data.Content;

import java.util.ArrayList;

public class DB_Manager extends SQLiteOpenHelper {
    public DB_Manager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DB_Manager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE Content (id INTEGER PRIMARY KEY AUTOINCREMENT, cost INTEGER,kind INTEGER,year INTEGER,month INTEGER,day INTEGER);";

        sqLiteDatabase.execSQL(query);
    }

    public ArrayList<Content> getDataList(Integer year, Integer month, Integer day) {
        ArrayList<Content> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from Content where year=" + year + " and month=" + month + " and day=" + day + ";";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(0);
            Integer cost = cursor.getInt(1);
            Integer kind = cursor.getInt(2);
            Integer y = cursor.getInt(3);
            Integer m = cursor.getInt(4);
            Integer d = cursor.getInt(5);

            Content item = new Content(id, cost, kind, y, m, d);
            items.add(item);
        }
        cursor.close();

        return items;
    }

    public void insertData(Integer cost, Integer kind, Integer year, Integer month, Integer day) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "Insert into Content values (null ," + cost + "," + kind + ", " + year + "," + month + "," + day + ");";

        db.execSQL(query);
    }

    public void deleteData(Integer id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "delete from Content where id= " + id + ";";

        db.execSQL(query);
    }

    public Integer get_today_income(Integer year, Integer month, Integer day) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select SUM(cost) from Content where kind = 0 and year=" + year + " and month=" + month + " and day=" + day + ";";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        Integer i = cursor.getInt(0);
        cursor.close();
        return i;
    }

    public Integer get_today_expense(Integer year, Integer month, Integer day) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select SUM(cost) from Content where kind = 1 and year=" + year + " and month=" + month + " and day=" + day + ";";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        Integer i = cursor.getInt(0);
        cursor.close();
        return i;
    }

    public Integer get_month_income(Integer year, Integer month) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select SUM(cost) from Content where kind = 0 and year=" + year + " and month=" + month + ";";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        Integer i = cursor.getInt(0);
        cursor.close();
        return i;
    }
    public Integer get_month_expense(Integer year, Integer month) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select SUM(cost) from Content where kind = 1 and year=" + year + " and month=" + month + ";";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        Integer i = cursor.getInt(0);
        cursor.close();
        return i;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
