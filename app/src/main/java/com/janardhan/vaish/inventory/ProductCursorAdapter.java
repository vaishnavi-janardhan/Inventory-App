package com.janardhan.vaish.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.janardhan.vaish.inventory.data.ProductContract.ProductEntry;


/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
class ProductCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = ProductCursorAdapter.class.getSimpleName();

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param cursor  The cursor from which to get the data.
     */
    ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
        int id = cursor.getInt(idColumnIndex);
        Log.e(LOG_TAG, "bindView: " + id);
        ImageButton btnAddCart = view.findViewById(R.id.btn_add_cart);
        ImageButton btnViewDetail = view.findViewById(R.id.btn_view_detail);
        btnAddCart.setTag(id);
        btnViewDetail.setTag(id);

        // Find the columns of product attributes that we're interested in
        final int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        final int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);


        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        int productPrice = cursor.getInt(priceColumnIndex);
        int productQuantity = cursor.getInt(quantityColumnIndex);

        TextView productNameTextView = view.findViewById(R.id.product_name_text_view);
        TextView productPriceTextView = view.findViewById(R.id.product_price_text_view);
        TextView productQuantityTextView = view.findViewById(R.id.product_quantity_text_view);

        // Update the TextViews with the attributes for the current product
        productNameTextView.setText(productName);
        productPriceTextView.setText(String.format("Rs.%s", String.valueOf(productPrice)));
        productQuantityTextView.setText(String.valueOf(productQuantity));

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rowId = (int) v.getTag();
                Log.e(LOG_TAG, "onClick: " + rowId);
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, rowId);
                String[] projection = new String[]{ProductEntry.COLUMN_PRODUCT_QUANTITY};
                Cursor currCursor = context.getContentResolver().query(currentProductUri, projection,
                        null, null, null);
                int quantity = 0;
                if (currCursor != null) {
                    Log.e(LOG_TAG, "cursor length: " + currCursor.getCount());
                    int quantityColumnIndex = currCursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
                    Log.e(LOG_TAG, "qty index: " + quantityColumnIndex);
                    currCursor.moveToFirst();
                    quantity = currCursor.getInt(quantityColumnIndex);
                    currCursor.close();
                }

                if (quantity == 0) {
                    Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity - 1);

                    int rowsUpdated = context.getContentResolver().update(currentProductUri, values,
                            null, null);
                    if (rowsUpdated > 0) {
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Unable to add to cart", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rowId = (int) v.getTag();
                Intent intent = new Intent(context, DetailActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, rowId);
                intent.setData(currentProductUri);
                context.startActivity(intent);
            }
        });
    }
}
