package androidbasicsnanodegree.sbl.InventoryAppV1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

//This contract class is inspired from the Udacity course : Android Basics Nanodegree - Data storage

public final class InventoryContract {

    // Following variables are used to create uri or perform query in the database
    public static final String CONTENT_AUTHORITY = "androidbasicsnanodegree.sbl.InventoryAppV1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEMS = "inventory";

    // Empty constructor created to prevent someone from accidentally instantiating the contract class,

    private InventoryContract() {
    }


    public static final class InventoryEntry implements BaseColumns {

        // Following variable is used to create content uris
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        // Following variables are used to precise the MIME type
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;


        // Following variables are used to read/write the database
        public final static String TABLE_NAME = "inventory";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME = "product_name";

        public final static String COLUMN_PRICE = "price";

        public final static String COLUMN_QUANTITY = "quantity";

        public final static String COLUMN_SUPPLIER = "supplier";

        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

    }
}