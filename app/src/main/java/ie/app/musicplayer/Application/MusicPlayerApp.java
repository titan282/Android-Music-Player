package ie.app.musicplayer.Application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.orm.SugarApp;

import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;


public class MusicPlayerApp extends SugarApp {

    public static final String CHANNEL_ID = "CHANNEL_MUSIC_APP";
    public List<Song> songList = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel music", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager!=null){
                manager.createNotificationChannel(channel);
            }
        }
    }


}
