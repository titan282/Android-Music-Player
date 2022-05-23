package ie.app.musicplayer.Application;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import com.orm.SugarApp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;


public class MusicPlayerApp extends SugarApp {

    public HashMap<String, ArrayList<Song>> album = new HashMap<>();
    public HashMap<String, ArrayList<Song>> singer = new HashMap<>();
    public static final String CHANNEL_ID = "CHANNEL_MUSIC_APP";
    public List<Song> songList = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        createFavoritePlaylist();
    }

    private void createFavoritePlaylist() {
        List<Playlist> playlists = Playlist.listAll(Playlist.class);
        if(playlists.size()==0){
            playlists.add(new Playlist("Favorites", R.drawable.favorites,new ArrayList<Song>()));
            playlists.get(0).save();
        }
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
