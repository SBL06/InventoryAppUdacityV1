package androidbasicsnanodegree.sbl.InventoryAppV1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidbasicsnanodegree.sbl.InventoryAppV1.data.InventoryContract;

//Following class and methods are inspired from the Udacity course : Android Basics Nanodegree - Data storage

public class Edit_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Unique identifier for the loader
    private static final int EXISTING_INVENTORY_LOADER = 0;

    // Following uri is used if an item has been clicked
    private Uri CurrentItemUri;

    // Declaring the EditText
    private EditText NameEditText;
    private EditText PriceEditText;
    private EditText SupplierEditText;
    private EditText SupplierPhoneEditText;
    private EditText QuantityEditText;

    // Variable in order to see if an item has been modified
    private boolean ItemHasChanged = false;

    // Setting a touch listener to know if an item has been modified
    private View.OnTouchListener TouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ItemHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Getting the uri from the main activity
        Intent intent = getIntent();
        CurrentItemUri = intent.getData();

        // Initializing the views
        NameEditText = findViewById(R.id.EditItemName);
        SupplierEditText = findViewById(R.id.EditSupplierView);
        SupplierPhoneEditText = findViewById(R.id.EditPhoneNumber);
        PriceEditText = findViewById(R.id.EditPriceView);
        QuantityEditText = findViewById(R.id.EditQuantityDisplay);

        // Setting up the touch listeners

        NameEditText.setOnTouchListener(TouchListener);
        SupplierPhoneEditText.setOnTouchListener(TouchListener);
        SupplierEditText.setOnTouchListener(TouchListener);
        PriceEditText.setOnTouchListener(TouchListener);
        QuantityEditText.setOnTouchListener(TouchListener);

        // Checking if we are in edit or create mode
        if (CurrentItemUri == null) {
            setTitle("New Item");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Item");

            // Initializing loader
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        // Setting the plus and minus button in the activity used to increase or decrease quantity
        Button minus = findViewById(R.id.MinusButton);
        Button plus = findViewById(R.id.PlusButton);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (QuantityEditText.getText().toString().isEmpty()) {
                    int quantity = 0;
                    QuantityEditText.setText(toString().valueOf(quantity));
                } else {
                    Integer quantity = Integer.parseInt(QuantityEditText.getText().toString().trim());
                    if (quantity == 0) {
                        Toast.makeText(Edit_Activity.this, "Quantity cannot be inferior to 0", Toast.LENGTH_SHORT).show();
                    } else {
                        quantity -= 1;
                        QuantityEditText.setText(quantity.toString());
                    }
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (QuantityEditText.getText().toString().isEmpty()) {
                    int quantity = 1;
                    QuantityEditText.setText(toString().valueOf(quantity));
                } else {
                    Integer quantity = Integer.parseInt(QuantityEditText.getText().toString().trim());
                    quantity += 1;
                    QuantityEditText.setText(quantity.toString());
                }
            }
        });

        // Setting up the order button used to call the provider

        Button orderButton = findViewById(R.id.OrderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Following code found on https://stackoverflow.com/questions/4275678/how-to-make-a-phone-call-using-intent-in-android
                String number = SupplierPhoneEditText.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });
    }

    // Setting up the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (CurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (ItemHasChanged) {
            super.onBackPressed();
        }

        // In case of unsaved change, showing a dialog
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);

    }

    private void saveItem() {
        // Getting the infos inputed in the fields
        String nameString = NameEditText.getText().toString().trim();
        String supplierString = SupplierEditText.getText().toString().trim();
        String priceString = PriceEditText.getText().toString().trim();
        String phoneString = SupplierPhoneEditText.getText().toString().trim();
        String quantityString = QuantityEditText.getText().toString().trim();

        // Creating a new contentvalues for the infos
        ContentValues values = new ContentValues();

        // Putting the datas in the contentvalues
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, R.string.name_required, Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, nameString);

        if (TextUtils.isEmpty(supplierString)) {
            Toast.makeText(this, R.string.supplier_required, Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER, supplierString);

        float price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            try {
                price = Float.parseFloat(priceString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.invalid_price, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, R.string.price_required, Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, price);

        long phone = 0;
        if (!TextUtils.isEmpty(phoneString)) {
            try {
                phone = Long.parseLong(phoneString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.invalid_phone, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, R.string.phone_required, Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER, phone);

        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            try {
                quantity = Integer.parseInt(quantityString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.invalid_quantity, Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, R.string.quantity_required, Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, quantity);

        // Checking if it is a new item or not
        if (CurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, R.string.insert_failed,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.item_inserted,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Updating the existing item
            int rowsAffected = getContentResolver().update(CurrentItemUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, R.string.update_failed,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.update_successful,
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    // Setting the actions of the buttons in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // Saving item
                saveItem();
                // exiting
                return true;
            // Deleting item
            case R.id.action_delete:
                // Confirmation message
                showDeleteConfirmationDialog();
                return true;
            // Action back button
            case android.R.id.home:
                // Asking confirmation in case of unsaved change
                if (!ItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(Edit_Activity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(Edit_Activity.this);
                            }
                        };

                // showing a dialog in case of unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Following methods sets up the dialog in case of unsaved changes
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsavedchange_dialog);
        builder.setPositiveButton(R.string.discard_unsaved, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Setting up the dialog in case of deletion
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.item_deleted);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel_deletion, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        // Deletion is only possible on an existing item
        if (CurrentItemUri != null) {

            int rowsDeleted = getContentResolver().delete(CurrentItemUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.delete_failed,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.item_deleted_correctly,
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    // Following methods are setting up the loader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE, InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER, InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        // Loading in the background thread
        return new CursorLoader(this,   // Parent activity context
                CurrentItemUri,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // If no data, returning early
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Retrieving the datas
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int SupplierColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER);
            int PhoneColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            int QuantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
            int PriceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);

            String itemName = cursor.getString(nameColumnIndex);
            String itemSupplier = cursor.getString(SupplierColumnIndex);
            String phoneNumber = toString().valueOf(cursor.getLong(PhoneColumnIndex));
            String Quantity = toString().valueOf(cursor.getInt(QuantityColumnIndex));
            String Price = toString().valueOf(cursor.getFloat(PriceColumnIndex));

            NameEditText.setText(itemName);
            SupplierEditText.setText(itemSupplier);
            SupplierPhoneEditText.setText(phoneNumber);
            QuantityEditText.setText(Quantity);
            PriceEditText.setText(Price);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Clearing the datas in case of invalidation
        NameEditText.setText("");
        SupplierEditText.setText("");
        SupplierPhoneEditText.setText("");
        PriceEditText.setText("");
        QuantityEditText.setText("");

    }
}
