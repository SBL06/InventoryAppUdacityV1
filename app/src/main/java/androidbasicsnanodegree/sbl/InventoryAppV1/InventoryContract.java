package androidbasicsnanodegree.sbl.InventoryAppV1;

import android.provider.BaseColumns;

public final class InventoryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {}


    public static final class InventoryEntry implements BaseColumns {


        public final static String TABLE_NAME = "inventory";


        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_PRODUCT_NAME ="product_name";


        public final static String COLUMN_PRICE = "price";


        public final static String COLUMN_QUANTITY = "quantity";


        public final static String COLUMN_SUPPLIER = "supplier";

        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";


    }

}