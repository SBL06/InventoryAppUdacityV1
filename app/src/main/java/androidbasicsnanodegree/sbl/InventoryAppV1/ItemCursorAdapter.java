package androidbasicsnanodegree.sbl.InventoryAppV1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidbasicsnanodegree.sbl.InventoryAppV1.data.InventoryContract;


public class ItemCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ItemCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
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
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = view.findViewById(R.id.NameView);
        TextView SupplierTextView = view.findViewById(R.id.SupplierView);
        TextView SupplierPhoneView = view.findViewById(R.id.SupplierPhoneView) ;
        TextView QuantityView = view.findViewById(R.id.QuantityView) ;
        TextView PriceView = view.findViewById(R.id.priceView) ;

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int SupplierColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER);
        int PhoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        int QuantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        int PriceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE) ;

        int idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID) ;
        final int id = cursor.getInt(idColumnIndex) ;
        final int quantity = cursor.getInt(QuantityColumnIndex) ;

        // Read the pet attributes from the Cursor for the current pet
        String itemName = cursor.getString(nameColumnIndex);
        String itemSupplier = cursor.getString(SupplierColumnIndex);
        String phoneNumber = toString().valueOf(cursor.getLong(PhoneColumnIndex));
        String Quantity = toString().valueOf(cursor.getInt(QuantityColumnIndex)) ;
        String Price = toString().valueOf(cursor.getFloat(PriceColumnIndex)) ;


        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(itemName);
        SupplierTextView.setText(itemSupplier);
        SupplierPhoneView.setText(phoneNumber);
        QuantityView.setText(context.getString(R.string.quantity_adapter) + Quantity);
        PriceView.setText(context.getString(R.string.price_adapter) + Price);




        // Thanks to runnerlt for the following code, found on https://github.com/runnerlt/Inventory/blob/master/app/src/main/java/com/example/android/inventory/InventoryCursorAdapter.java#L68

        Button SaleButton = view.findViewById(R.id.SaleButton) ;
        SaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) context ;
                activity.saleButton(id, quantity);
            }
        });

        Button EditButton = view.findViewById(R.id.EditButton) ;
        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) context ;
                activity.editButton(id);
            }
        });
    }
}
