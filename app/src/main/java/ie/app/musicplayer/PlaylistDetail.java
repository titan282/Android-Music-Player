package ie.app.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;

public class PlaylistDetail extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView playlistImage;
    String playlistName;
    ArrayList<Song> songs= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        recyclerView = findViewById(R.id.playlistRecycleView);
        playlistImage = findViewById(R.id.playlistPhoto);
        playlistName = getIntent().getStringExtra("playlistName");
    }
}