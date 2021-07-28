package com.example.easycart.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.easycart.Model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    // Constant
    public static final String DATABASE_NAME = "ITEM_DATABASE";
    public static final String MYDATABASE_TABLE = "MY_ITEM_TABLE";
    public static final int DATABASE_VERSION = 1;
    public static final String ID_COL = "ID";
    public static final String NAME_COL = "NAME";
    public static final String PRICE_COL = "PRICE";
    public static final String QUANTITY_COL = "QUANTITY";
    private SQLiteDatabase sqLiteDatabase;


    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MYDATABASE_TABLE
                + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL, PRICE REAL, QUANTITY INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MYDATABASE_TABLE);
        onCreate(db);
    }

    public void insertItem(ItemModel item) {
        // get permission to write
        sqLiteDatabase = this.getWritableDatabase();
        // FIRST TABLE
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME_COL, item.getName());
        contentValues.put(PRICE_COL, item.getPrice());
        contentValues.put(QUANTITY_COL, item.getQuantity());


        sqLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
    }

    public void updateName(int id, String name) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COL, name);

        sqLiteDatabase.update(MYDATABASE_TABLE, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updatePrice(int id, double price) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRICE_COL, price);

        sqLiteDatabase.update(MYDATABASE_TABLE, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateQuantity(int id, int qty) {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUANTITY_COL, qty);

        sqLiteDatabase.update(MYDATABASE_TABLE, contentValues, "ID=?", new String[]{String.valueOf(id)});
    }

    public void itemDelete(int id) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MYDATABASE_TABLE, "ID=?", new String[]{String.valueOf(id)});
    }

    public List<ItemModel> getAllItem() {
        sqLiteDatabase = this.getWritableDatabase();
        List<ItemModel> itemModelList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                null);

        // Columns index
        int id_index = cursor.getColumnIndex(ID_COL);
        int name_index = cursor.getColumnIndex(NAME_COL);
        int price_index = cursor.getColumnIndex(PRICE_COL);
        int quantity_index = cursor.getColumnIndex(QUANTITY_COL);

        sqLiteDatabase.beginTransaction();
        try {
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                ItemModel item = new ItemModel();
                item.setId(cursor.getInt(id_index));
                item.setName(cursor.getString(name_index));
                item.setPrice(cursor.getDouble(price_index));
                item.setQuantity(cursor.getInt(quantity_index));
                itemModelList.add(item);
            }
        } finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }
        return itemModelList;
    }

    public double getTotalPrice(){
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE,
                new String[]{PRICE_COL, QUANTITY_COL},
                null,
                null,
                null,
                null,
                null);

        // Columns index
        int price_col_index = cursor.getColumnIndex(PRICE_COL);
        int quantity_col_index = cursor.getColumnIndex(QUANTITY_COL);
        double total = 0;

        sqLiteDatabase.beginTransaction();
        try {
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                ItemModel item = new ItemModel();
                total = total + (cursor.getDouble(price_col_index)*cursor.getDouble(quantity_col_index));
            }
        } finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }
        return total;
    }

    public String displayAll() {
        sqLiteDatabase = this.getReadableDatabase();
        List<ItemModel> itemModelList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(MYDATABASE_TABLE, null,
                null, null, null, null, null);

        // Columns index
        int index_CONTENT1 = cursor.getColumnIndex(NAME_COL);
        int index_CONTENT2 = cursor.getColumnIndex(PRICE_COL);
        int index_CONTENT3 = cursor.getColumnIndex(QUANTITY_COL);
        String result = "";
        sqLiteDatabase.beginTransaction();
        try {
            for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
                result = result + cursor.getString(index_CONTENT1) + "; "
                        + cursor.getInt(index_CONTENT2) + "; "
                        + cursor.getInt(index_CONTENT3)
                        + "\n";
            }
        } finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }
        return result;
    }

    public void deleteAll() {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MYDATABASE_TABLE, null, null);
    }
}