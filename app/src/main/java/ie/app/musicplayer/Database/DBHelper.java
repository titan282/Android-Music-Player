package ie.app.musicplayer.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String SONG_TABLE = "Songs";
    public static final String SONG_ID = "SongID";
    public static final String SONG_NAME = "SongName";
    public static final String SONG_ALBUM = "SongAlbum";
    public static final String SONG_IMG = "SongImage";
    public static final String SONG_SINGER = "SongSinger";
    public static final String SONG_URL = "SongURL";

    public static final String PLAYLIST_TABLE = "Playlists";
    public static final String PLAYLIST_ID = "PlaylistID";
    public static final String PLAYLIST_NAME = "PlaylistName";
    public static final String PLAYLIST_IMG = "PlaylistImage";

    private static final String DATABASE_NAME = "musicplayer.db";
    private static final int VERSION = 1;
    // Song: ID, Name, Album, img, singer, url, playlistID
    private static final String DATABASE_CREATE_TABLE_SONG =
            String.format("CREATE TABLE IF NOT EXISTS %s" +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s INTEGER NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s INTEGER);",SONG_TABLE,SONG_ID,SONG_NAME,SONG_ALBUM,SONG_IMG, SONG_SINGER, SONG_URL, PLAYLIST_ID);

    // Playlists(ID,Name)
    private static final String DATABASE_CREATE_TABLE_PLAYLIST =
            String.format("CREATE TABLE IF NOT EXISTS %s " +
            "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            "%s TEXT);",PLAYLIST_TABLE,PLAYLIST_ID,PLAYLIST_NAME);
    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    //Function for CREATE, DELETE, UPDATE,...
    public void query(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //Get Data:Select
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_TABLE_SONG);
        sqLiteDatabase.execSQL(DATABASE_CREATE_TABLE_PLAYLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
