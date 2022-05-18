package ie.app.musicplayer.Application;

import com.orm.SugarApp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ie.app.musicplayer.Model.Song;


public class MusicPlayerApp extends SugarApp {


    public HashMap<String, ArrayList<Song>> album = new HashMap<>();
    public HashMap<String, ArrayList<Song>> singer = new HashMap<>();
    public List<Song> songList = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
    }

}
