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

//Following class and methods are inspired from the Udacity course : Android Basics Nanodegree - Data storage

public class ItemCursorAdapter extends CursorAdapter {

    // empty constructor
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // inflating the view
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    // setting up each views in the list
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameTextView = view.findViewById(R.id.NameView);
        TextView SupplierTextView = view.findViewById(R.id.SupplierView);
        TextView SupplierPhoneView = view.findViewById(R.id.SupplierPhoneView);
        TextView QuantityView = view.findViewById(R.id.QuantityView);
        TextView PriceView = view.findViewById(R.id.priceView);

        // retrieving datas
        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int SupplierColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER);
        int PhoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        int QuantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        int PriceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);

        int idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
        final int id = cursor.getInt(idColumnIndex);
        final int quantity = cursor.getInt(QuantityColumnIndex);

        // Reading attributes
        String itemName = cursor.getString(nameColumnIndex);
        String itemSupplier = cursor.getString(SupplierColumnIndex);
        String phoneNumber = toString().valueOf(cursor.getLong(PhoneColumnIndex));
        String Quantity = toString().valueOf(cursor.getInt(QuantityColumnIndex));
        String Price = toString().valueOf(cursor.getFloat(PriceColumnIndex));

        // Updating the views
        nameTextView.setText(itemName);
        SupplierTextView.setText(itemSupplier);
        SupplierPhoneView.setText(phoneNumber);
        QuantityView.setText(context.getString(R.string.quantity_adapter) + Quantity);
        PriceView.setText(context.getString(R.string.price_adapter) + Price);

        // Following code found on https://github.com/runnerlt/Inventory/blob/master/app/src/main/java/com/example/android/inventory/InventoryCursorAdapter.java#L68
        Button SaleButton = view.findViewById(R.id.SaleButton);
        SaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) context;
                activity.saleButton(id, quantity);
            }
        });

        Button EditButton = view.findViewById(R.id.EditButton);
        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) context;
                activity.editButton(id);
            }
        });
    }
}
