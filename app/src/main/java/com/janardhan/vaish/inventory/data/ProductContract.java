package com.janardhan.vaish.inventory.data;

import android.provider.BaseColumns;

public final class ProductContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ProductContract() {}

    //Inner class that defines the table contents
    public static class ProductEntry implements BaseColumns {

        /** Name of database table for pets */
        public static final String TABLE_NAME = "products";

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
