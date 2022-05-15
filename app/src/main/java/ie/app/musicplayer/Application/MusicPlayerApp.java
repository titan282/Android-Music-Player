package ie.app.musicplayer.Application;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;


public class MusicPlayerApp extends Application {

//    public DBManager dbManager;
    public List<Song> songList = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
//        dbManager = new DBManager(this);
//        dbManager.addPlaylist("Love");
    }

}
