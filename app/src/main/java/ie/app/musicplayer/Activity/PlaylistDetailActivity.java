package ie.app.musicplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class PlaylistDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView playlistImage;
    String playlistName;
    ArrayList<Song> songs= new ArrayList<>();

    DBManager dbManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(PlaylistDetailActivity.this);
        setContentView(R.layout.activity_playlist_detail);
        recyclerView = findViewById(R.id.playlistRecycleView);
        playlistImage = findViewById(R.id.playlistPhoto);
        playlistName = getIntent().getStringExtra("playlistName");
    }
}