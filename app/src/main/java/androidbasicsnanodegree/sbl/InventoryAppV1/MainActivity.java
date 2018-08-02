package androidbasicsnanodegree.sbl.InventoryAppV1;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import androidbasicsnanodegree.sbl.InventoryAppV1.data.InventoryContract;

public class MainActivity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor> {

    ItemCursorAdapter adapter ;

    private static final int ITEM_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Edit_Activity.class);
                startActivity(intent);
            }
        });

        adapter = new ItemCursorAdapter(this, null) ;

        ListView list = findViewById(R.id.list) ;
        list.setEmptyView(findViewById(R.id.empty_view));

        list.setAdapter(adapter);


        getLoaderManager().initLoader(ITEM_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_dummy:
                insertItem();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void insertItem() {

        ContentValues dummyValues = new ContentValues() ;

        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, "DummyItem");
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_PRICE, 25);
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, 1);
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER, "DummySupply");
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER, 4383569874l);

        getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, dummyValues) ;
    }

    private void deleteAll() {

        getContentResolver().delete(InventoryContract.InventoryEntry.CONTENT_URI, null, null) ;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE, InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER, InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                InventoryContract.InventoryEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public void saleButton(int id, int quantity) {

        Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

        ContentValues values = new ContentValues() ;
        int newQuantity ;

        if (quantity < 1) {
            newQuantity = 0 ;
        }
        else {
            newQuantity = quantity -1 ;
        }

        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, newQuantity) ;

        getContentResolver().update(currentItemUri, values, null, null );

    }

    public void editButton (int id) {

        Intent intent = new Intent(MainActivity.this, Edit_Activity.class);

        // Form the content URI that represents the specific pet that was clicked on,
        // by appending the "id" (passed as input to this method) onto the
        // {@link PetEntry#CONTENT_URI}.
        // For example, the URI would be "content://com.example.android.pets/pets/2"
        // if the pet with ID 2 was clicked on.
        Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);

        // Set the URI on the data field of the intent
        intent.setData(currentItemUri);

        Toast.makeText(this, currentItemUri.toString(), Toast.LENGTH_SHORT).show();

        // Launch the {@link EditorActivity} to display the data for the current pet.
        startActivity(intent);
    }
}
