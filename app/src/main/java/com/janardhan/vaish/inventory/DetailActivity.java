package com.janardhan.vaish.inventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.janardhan.vaish.inventory.data.ProductContract.ProductEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the product data loader
     */
    private static final int EXISTING_PRODUCT_LOADER = 0;
    @BindView(R.id.product_name_text_view)
    TextView productNameTextView;
    @BindView(R.id.product_price_text_view)
    TextView productPriceTextView;
    @BindView(R.id.product_quantity_text_view)
    TextView productQuantityTextView;
    @BindView(R.id.supplier_name_text_view)
    TextView supplierNameTextView;
    @BindView(R.id.supplier_no_text_view)
    TextView supplierNoTextView;
    /**
     * Content URI for the existing product
     */
    private Uri currentProductUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        currentProductUri = intent.getData();

        getSupportLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
    }

    @OnClick(R.id.fab_call)
    public void onClick(View view) {
        Cursor cursor = getContentResolver().query(currentProductUri,
                new String[]{ProductEntry.COLUMN_SUPPLIER_NUMBER}, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        long phoneNumber = 0;
        if (cursor != null) {
            phoneNumber = cursor.getLong(cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NUMBER));
        }
        cursor.close();
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    @OnClick(R.id.btn_qty_add)
    public void increaseQty(View view) {
        Cursor cursor = getContentResolver().query(currentProductUri,
                new String[]{ProductEntry.COLUMN_PRODUCT_QUANTITY}, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int quantity = 0;
        if (cursor != null) {
            quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        }
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity + 1);
        int rowsUpdated = getContentResolver().update(currentProductUri, values, null, null);
        if (rowsUpdated == 0) {
            Toast.makeText(this, "Not possible", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    @OnClick(R.id.btn_qty_sub)
    public void decreaseQty(View view) {
        Cursor cursor = getContentResolver().query(currentProductUri,
                new String[]{ProductEntry.COLUMN_PRODUCT_QUANTITY}, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int quantity = 0;
        if (cursor != null) {
            quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        }

        if (quantity > 0) {
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity - 1);
            int rowsUpdated = getContentResolver().update(currentProductUri, values, null, null);
            if (rowsUpdated == 0) {
                Toast.makeText(this, "Not possible", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Not possible", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        MenuItem menuItem = menu.findItem(R.id.action_save);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Prompt the user to confirm that they want to delete this product.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * Perform the deletion of the product in the database.
     */
    private void deleteProduct() {
        // Only perform the delete if this is an existing product.
        if (currentProductUri != null) {
            // Call the ContentResolver to delete the product at the given content URI.
            // Pass in null for the selection and selection args because the currentProductUri
            // content URI already identifies the product that we want.
            int rowsDeleted = getContentResolver().delete(currentProductUri, null,
                    null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        // Since the editor shows all product attributes, define a projection that contains
        // all columns from the product table
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_SUPPLIER_NAME,
                ProductEntry.COLUMN_SUPPLIER_NUMBER};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                currentProductUri,              // Query the content URI for the current product
                projection,                     // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of product attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NAME);
            int supplierNoColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SUPPLIER_NUMBER);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            long supplierNo = cursor.getLong(supplierNoColumnIndex);

            // Update the views on the screen with the values from the database
            productNameTextView.setText(name);
            productPriceTextView.setText(String.format("Rs.%s", String.valueOf(price)));
            productQuantityTextView.setText(String.valueOf(quantity));
            supplierNameTextView.setText(supplierName);
            supplierNoTextView.setText(String.valueOf("Tel: " + supplierNo));

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        productNameTextView.setText("");
        productPriceTextView.setText("");
        productQuantityTextView.setText("");
        supplierNameTextView.setText("");
        supplierNoTextView.setText("");
    }
}