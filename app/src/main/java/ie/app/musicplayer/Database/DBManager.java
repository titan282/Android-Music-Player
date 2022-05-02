package ie.app.musicplayer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;

public class DBManager {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBManager(@Nullable Context context){
        dbHelper = new DBHelper(context);
    }
    public void open()  {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public void addPlaylist(String playlistName){
        open();
        ContentValues values = new ContentValues();
        values.put(DBHelper.PLAYLIST_NAME,playlistName);
        database.insert(DBHelper.PLAYLIST_TABLE,null,values);
        close();
    }
    public void addSong(Song song){
    }

    // get all songs from playlist
    public List<Song> getAllSong(String PlaylistName){
        List<Song> songList = new ArrayList<Song>();
        Cursor cursor = dbHelper.getData(
                // SELECT * FROM songs INNER JOIN playlist ON songs.playlistID = playlist.playlistID
                // WHERE playlistName = PlaylistName;
             String.format("SELECT * FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s = %s",
                     DBHelper.SONG_TABLE,DBHelper.PLAYLIST_TABLE,DBHelper.SONG_TABLE,
                     DBHelper.PLAYLIST_ID,DBHelper.PLAYLIST_TABLE,DBHelper.PLAYLIST_ID,
                     DBHelper.PLAYLIST_NAME,PlaylistName)
        );
       cursor.moveToFirst();
       while(!cursor.isAfterLast()){
            songList.add(getSongFromCursor(cursor));
            cursor.moveToNext();
       }
       cursor.close();
       return songList;
    }

    // add 1 song to playlist
    public void addSongToPlaylist(Song song, Playlist playlist){
        ContentValues values = new ContentValues();
        values.put(DBHelper.SONG_NAME,song.getSongName());
        values.put(DBHelper.SONG_ALBUM,song.getSongAlbum());
        values.put(DBHelper.SONG_IMG,song.getSongImage());
        values.put(DBHelper.SONG_SINGER,song.getSongSinger());
        values.put(DBHelper.SONG_URL,song.getSongURL());
        values.put(DBHelper.PLAYLIST_ID,getPlaylistID(playlist.getPlaylistName()));
        database.insert(DBHelper.SONG_TABLE,null,values);
    }

    public int getPlaylistID(String playlistName){
        final String SELECT_PLAYLIST_ID = String.format(
                "SELECT %s FROM %s WHERE %s = %s",
                DBHelper.PLAYLIST_ID,DBHelper.PLAYLIST_TABLE,
                DBHelper.PLAYLIST_NAME,playlistName
        );
        Cursor cursor = dbHelper.getData(SELECT_PLAYLIST_ID);
        return cursor.getInt(0);
    }


    public Song getSongFromCursor(Cursor cursor){
        Song song = new Song();
        song.setSongId(cursor.getInt(0));
        song.setSongName(cursor.getString(1));
        song.setSongAlbum(cursor.getString(2));
        song.setSongImage(cursor.getInt(3));
        song.setSongSinger(cursor.getString(4));
        song.setSongURL(cursor.getString(5));
        return song;
    }

    public void reset(){
        database.delete(dbHelper.SONG_TABLE, null,null);
        database.delete(dbHelper.PLAYLIST_TABLE, null,null);
    }
}
