package androidbasicsnanodegree.sbl.InventoryAppV1;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidbasicsnanodegree.sbl.InventoryAppV1.data.InventoryContract;

// Following class and methods are inspired from the Udacity course : Android Basics Nanodegree - Data storage

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // Initializing the adapter
    ItemCursorAdapter adapter;

    // Unique identifier for the loader
    private static final int ITEM_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Edit_Activity.class);
                startActivity(intent);
            }
        });

        // Creating a new custom adapter
        adapter = new ItemCursorAdapter(this, null);

        // Initializing the list view
        ListView list = findViewById(R.id.list);
        list.setEmptyView(findViewById(R.id.empty_view));
        // setting the adapter
        list.setAdapter(adapter);
        // setting the onclicklistener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Edit_Activity.class);
                Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, l);
                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });
        // Initializing the loader
        getLoaderManager().initLoader(ITEM_LOADER, null, this);

    }

    //Setting up the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Insert dummy button
            case R.id.action_dummy:
                insertItem();
                return true;
            // Delete all item
            case R.id.action_delete_all:
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method used to insert a dummy item
    private void insertItem() {
        ContentValues dummyValues = new ContentValues();
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, "DummyItem");
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_PRICE, 25);
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, 1);
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER, "DummySupply");
        dummyValues.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER, 4383569874l);
        getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, dummyValues);
    }

    // Method to delete all the items
    private void deleteAll() {
        getContentResolver().delete(InventoryContract.InventoryEntry.CONTENT_URI, null, null);
    }

    // Following methods set up and configure the loader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryContract.InventoryEntry.COLUMN_PRICE, InventoryContract.InventoryEntry.COLUMN_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_SUPPLIER, InventoryContract.InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        // Retrieving data on the background thread
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

    // Following method is called when the sale button is clicked
    public void saleButton(int id, int quantity) {
        Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);
        ContentValues values = new ContentValues();
        int newQuantity;

        if (quantity < 1) {
            newQuantity = 0;
        } else {
            newQuantity = quantity - 1;
        }
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, newQuantity);
        getContentResolver().update(currentItemUri, values, null, null);
    }

    // Following method is called when the edit button is clicked
    public void editButton(int id) {
        Intent intent = new Intent(MainActivity.this, Edit_Activity.class);
        Uri currentItemUri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);
        intent.setData(currentItemUri);
        startActivity(intent);
    }
}
