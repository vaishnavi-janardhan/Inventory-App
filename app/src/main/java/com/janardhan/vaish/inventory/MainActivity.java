package com.janardhan.vaish.inventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.janardhan.vaish.inventory.data.ProductContract.ProductEntry;
import com.janardhan.vaish.inventory.data.ProductDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Displays list of products that were entered and stored in the app.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.display_text_view)
    TextView displayTextView;

    //Database helper that will provide us access to the database
    private ProductDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        dbHelper = new ProductDbHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the products database.
     */
    private void displayDatabaseInfo() {
        //Create and/or open a database to read from it
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_NUMBER,
        };
        // Perform a query on the pets table
        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayTextView.setText("The table contains " + cursor.getCount() + " products\n\n");
            displayTextView.append(
                    ProductEntry._ID + " - "
                            + ProductEntry.COLUMN_PRODUCT_NAME + " - "
                            + ProductEntry.COLUMN_PRODUCT_PRICE + " - "
                            + ProductEntry.COLUMN_PRODUCT_QUANTITY + " - "
                            + ProductEntry.COLUMN_SUPPLIER_NAME + " - "
                            + ProductEntry.COLUMN_SUPPLIER_NUMBER + "\n\n");

            //Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierNoColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NUMBER);

            //Iterate through all returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                long currentSupplierNo = cursor.getLong(supplierNoColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayTextView.append(
                        currentId + " - "
                                + currentName + " - "
                                + currentPrice + " - "
                                + currentQuantity + " - "
                                + currentSupplierName + " - "
                                + currentSupplierNo + "\n"
                );
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    @OnClick(R.id.fab_add)
    public void onClick(View view) {
        insertDummyProduct();
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertDummyProduct() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "Book");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, 100);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, 10);
        contentValues.put(ProductEntry.COLUMN_SUPPLIER_NAME, "Udacity");
        contentValues.put(ProductEntry.COLUMN_SUPPLIER_NUMBER, 9234567890d);

        db.insert(ProductEntry.TABLE_NAME, null, contentValues);

        displayDatabaseInfo();
    }
}
