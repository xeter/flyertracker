package org.xeter.flyertracker.android.db;

import android.provider.BaseColumns;

public final class FlyerReaderContract {
    private FlyerReaderContract() {}

    public static final String FTS_TABLE_CREATE =
            "CREATE VIRTUAL TABLE " + FlyerEntry.TABLE_NAME +
                    " USING fts3 (" +
                    FlyerEntry.COLUMN_NAME_TITLE + " text not null, " +
                    FlyerEntry.COLUMN_NAME_DESCRIPTION + " text," +
                    FlyerEntry.COLUMN_NAME_IMG_PATH + " text" +
                    ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FlyerEntry.TABLE_NAME;

    public static class FlyerEntry implements BaseColumns {

        public static final String TABLE_NAME = "FLYER";

        public static final String FLYER_ID = "rowid";
        public static final String COLUMN_NAME_TITLE = "TITLE";
        public static final String COLUMN_NAME_DESCRIPTION = "DESCRIPTION";
        public static final String COLUMN_NAME_IMG_PATH = "IMG_PATH";
    }
}
