package com.janardhan.vaish.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.janardhan.vaish.inventory.data.ProductContract.ProductEntry;

public class ProductDbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = ProductDbHelper.class.getSimpleName();


    /**
     * Constructs a new instance of {@link ProductDbHelper}
     *
     * @param context of the app
     */
    public ProductDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        //CREATE TABLE products(_id INTEGER PRIMARY KEY, name TEXT, price INTEGER, quantity INTEGER,
        // supplier_name TEXT, supplier_phone LONG)
        String SQL_CREATE_PRODUCTS_TABLE =
                "CREATE TABLE "
                + ProductEntry.TABLE_NAME + "("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT, "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER, "
                + ProductEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + ProductEntry.COLUMN_SUPPLIER_NAME + " TEXT, "
                + ProductEntry.COLUMN_SUPPLIER_NUMBER + " BIGINT);";

        Log.d(TAG, "schema: " + SQL_CREATE_PRODUCTS_TABLE);

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int getLongVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
