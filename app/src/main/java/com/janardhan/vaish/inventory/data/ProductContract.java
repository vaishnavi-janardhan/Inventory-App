package com.janardhan.vaish.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ProductContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ProductContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    static final String CONTENT_AUTHORITY = "com.janardhan.vaish.inventory";
    /**
     * Possible path (appended to base content URI for possible URI's)
     */
    static final String PATH_PRODUCTS = "products";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single product.
     */
    public static class ProductEntry implements BaseColumns {

        /**
         * The content URI to access the product data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /** Name of database table for pets */
        static final String TABLE_NAME = "products";

        /**
         * Unique ID number for the product (only for use in the database table).
         *
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Name of the product
         *
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_NAME = "name";

        /**
         * PRICE of the product
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Quantity of the product
         *
         * Type: INTEGER
         */
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * Name of the supplier
         *
         * Type: TEXT
         */
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";

        /**
         * Phone number of the supplier
         *
         * Type: LONG/BIGINT
         */
        public static final String COLUMN_SUPPLIER_NUMBER = "supplier_no";

    }

}
