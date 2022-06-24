package ie.app.musicplayer.Application;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.orm.SugarApp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;


public class MusicPlayerApp extends SugarApp {

    public Map<String, ArrayList<Song>> album = new TreeMap<>();
    public Map<String, ArrayList<Song>> singer = new TreeMap<>();
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
            channel.setSound(null,null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager!=null){
                manager.createNotificationChannel(channel);
            }
        }
    }
    public static void sendNotificationMedia(Song song, Context context) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context,"tag");
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, MusicPlayerApp.CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLargeIcon(song.getSongEmbeddedPicture())
                .setSubText("MusicPlayer")
                .setContentTitle(song.getSongName())
                .setSubText(song.getSongSinger())
                .setSmallIcon(R.drawable.ic_music)
                .addAction(R.drawable.ic_skip_previous, "Previous", null) // #0
                .addAction(R.drawable.ic_pause, "Pause", null)  // #1
                .addAction(R.drawable.ic_skip_next, "Next", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1 /* #1: pause button */)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setOngoing(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        Log.v("song", song.toString());
        managerCompat.notify(100,notification.build());
    }
}
