package kareemahmed.spotifyxtremeedition;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
// This is my content provider and sql database this class is based off some example code i found online ive just changed it so it will work with my system.
public class PlaylistProvider extends ContentProvider {
    static final String PROVIDER_NAME = "kareemahmed.spotifyxtremeedition.PlaylistProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/playlists";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "name";
    private SQLiteDatabase db;

    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    static final int Playlists = 1;
    static final int Playlist_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "playlists", Playlists);
        uriMatcher.addURI(PROVIDER_NAME, "playlists/#", Playlist_ID);
    }

    /**
     * Database specific constant declarations
     */
    //information of database
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "playlists.db";
    public static final String TABLE_NAME = "Playlists";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_NOOFSONGS = "AmountOfSongs";
    public static final String COLUMN_IMAGE = "ImageURL";
    public static final String COLUMN_USERID = "UserID";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_NOOFSONGS + " INTEGER,"
                    + COLUMN_IMAGE + " TEXT,"
                    + COLUMN_USERID + " TEXT"
                    + ")";

    public class MyDBHandler extends SQLiteOpenHelper {
        //initialize the database
        public MyDBHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    @Override
    public boolean onCreate() {
        Context context = getContext();
        MyDBHandler dbHelper = new MyDBHandler(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(	TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case Playlists:
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                break;

            case Playlist_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = NAME;
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case Playlists:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case Playlist_ID:
                String id = uri.getPathSegments().get(1);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case Playlists:
                count = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;

            case Playlist_ID:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case Playlists:
                return "vnd.android.cursor.dir/vnd.example.playlists";
            /**
             * Get a particular student
             */
            case Playlist_ID:
                return "vnd.android.cursor.item/vnd.example.playlists";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}