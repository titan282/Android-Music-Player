package ie.app.musicplayer.Application;

import android.app.Application;

import ie.app.musicplayer.Database.DBManager;


public class MusicPlayerApp extends Application {

    public DBManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = new DBManager(this);
    }

}
