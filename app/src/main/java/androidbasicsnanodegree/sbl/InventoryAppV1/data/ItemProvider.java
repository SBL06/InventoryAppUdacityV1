package androidbasicsnanodegree.sbl.InventoryAppV1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import androidbasicsnanodegree.sbl.InventoryAppV1.R;

// Following class and methods contained are inspired from the Udacity course : Android Basics Nanodegree - Data storage
public class ItemProvider extends ContentProvider {

    // Following variables are used in the uri matcher

    private static final int ITEMS = 00;

    private static final int ITEMS_ID = 01;

    // Following matcher checks if the uri is correct

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ITEMS, ITEMS);

        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ITEMS + "/#", ITEMS_ID);
    }

    private InventoryDbHelper DbHelper;

    @Override
    public boolean onCreate() {
        DbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Getting readable database and creating a cursor
        SQLiteDatabase database = DbHelper.getReadableDatabase();
        Cursor cursor;

        // Checking the uri
        int match = sUriMatcher.match(uri);
        switch (match) {

            case ITEMS:
                // Performing a query in the whole database
                cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case ITEMS_ID:
                // performing a query only for the specified ID.
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(InventoryContract.InventoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                cursor = null;
        }
        if (cursor != null) {
            // Setting the notification uri for the cursor.
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.insert_illeegalexception) + uri);
        }
    }

    // Following method inserts a new item in the database

    private Uri insertItem(Uri uri, ContentValues values) {

        // Getting the database
        SQLiteDatabase database = DbHelper.getWritableDatabase();

        // Inserting the new item
        long id = database.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
        // if id = -1, insertion failed
        if (id == -1) {
            return null;
        }

        // Notifying that the datas changed
        getContext().getContentResolver().notifyChange(uri, null);

        // returning the new uri according to the new id
        return ContentUris.withAppendedId(uri, id);
    }

    // Following method is updating the database
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEMS_ID:
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    // Following method is used in order to update a selected item in the database
    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Getting the database
        SQLiteDatabase database = DbHelper.getWritableDatabase();

        // Updating the database
        int rowsUpdated = database.update(InventoryContract.InventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        // Notifying a change in the datas
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    // Following method is used to delete something in the database

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = DbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS_ID:
                // Delete a single row given by the ID in the URI
                selection = InventoryContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // Notifying a change in the datas
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    // Following method is used to specify the MIME type
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return InventoryContract.InventoryEntry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return InventoryContract.InventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
