package ie.app.musicplayer.Application;

import android.app.Application;

import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Model.Playlist;


public class MusicPlayerApp extends Application {

    public DBManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = new DBManager(this);
//        dbManager.addPlaylist("Love");
    }

}
