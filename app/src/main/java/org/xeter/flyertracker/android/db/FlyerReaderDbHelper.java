package org.xeter.flyertracker.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.xeter.flyertracker.android.R;
import org.xeter.flyertracker.android.flyer.list.Flyer;

import java.util.Iterator;

public class FlyerReaderDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "flyers.db";
    private static final int DATABASE_VERSION = 1;

    public FlyerReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FlyerReaderContract.FTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FlyerReaderContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Flyer addFlyer(Flyer flyer) {
        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues values = new ContentValues();
        if (flyer.getId() != 0L) {
            values.put(FlyerReaderContract.FlyerEntry.FLYER_ID, flyer.getId());
        }
        values.put(FlyerReaderContract.FlyerEntry.COLUMN_NAME_TITLE, flyer.getTitle());
        values.put(FlyerReaderContract.FlyerEntry.COLUMN_NAME_DESCRIPTION, flyer.getDescription());
        values.put(FlyerReaderContract.FlyerEntry.COLUMN_NAME_IMG_PATH, flyer.getImagePath());

        final long newRowId = db.insertWithOnConflict(FlyerReaderContract.FlyerEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return new Flyer(newRowId, flyer);
    }

    public Iterator<Flyer> getAllFlyers() {

        final SQLiteDatabase db = getReadableDatabase();

        final Cursor cursor = db.query(
                FlyerReaderContract.FlyerEntry.TABLE_NAME,                     // The table to query
                new String[]{FlyerReaderContract.FlyerEntry.FLYER_ID, FlyerReaderContract.FlyerEntry.COLUMN_NAME_TITLE, FlyerReaderContract.FlyerEntry.COLUMN_NAME_DESCRIPTION, FlyerReaderContract.FlyerEntry.COLUMN_NAME_IMG_PATH},                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        return new FlyerIterator(cursor);
    }

    public Iterator<Flyer> getFlyersMatching(String query) {

        final SQLiteDatabase db = getReadableDatabase();
        final String enhancedQuery = query + "*";
        final String sql = "select " + FlyerReaderContract.FlyerEntry.FLYER_ID + ", " +
                FlyerReaderContract.FlyerEntry.COLUMN_NAME_TITLE + ", " +
                FlyerReaderContract.FlyerEntry.COLUMN_NAME_DESCRIPTION + ", " +
                FlyerReaderContract.FlyerEntry.COLUMN_NAME_IMG_PATH +
                " from " + FlyerReaderContract.FlyerEntry.TABLE_NAME +
                " where " + FlyerReaderContract.FlyerEntry.TABLE_NAME + " match ?";
        final Cursor cursor = db.rawQuery(sql, new String[]{enhancedQuery});

        return new FlyerIterator(cursor);
    }

    public static class FlyerIterator implements Iterator<Flyer> {

        private final Cursor cursor;

        public FlyerIterator(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor.moveToNext();
        }

        @Override
        public Flyer next() {
            final int id = getInt(FlyerReaderContract.FlyerEntry.FLYER_ID);
            final String title = getString(FlyerReaderContract.FlyerEntry.COLUMN_NAME_TITLE);
            final String description = getString(FlyerReaderContract.FlyerEntry.COLUMN_NAME_DESCRIPTION);
            final String imagePath = getString(FlyerReaderContract.FlyerEntry.COLUMN_NAME_IMG_PATH);
            final Flyer flyer = new Flyer(id, title, description, imagePath);
            if (cursor.isLast()) {
                cursor.close();
            }
            return flyer;
        }

        private String getString(String columnName) {
            return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
        }

        private int getInt(String columnName) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
        }
    }
}
