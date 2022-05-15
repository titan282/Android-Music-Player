package ie.app.musicplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Fragment.SongFragment;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class PlaylistDetailActivity extends AppCompatActivity {

    RecyclerView rvSongList;
    ImageView playlistImage;
    int playlistId;
    List<Song> songList= new ArrayList<>();
    List<Playlist> playlists ;
    Playlist playlist;
    DBManager dbManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(PlaylistDetailActivity.this);
        setContentView(R.layout.activity_playlist_detail);
        rvSongList = findViewById(R.id.playlistRecycleView);
        playlistImage = findViewById(R.id.playlistPhoto);
        playlistId = 2;
        playlists = Playlist.listAll(Playlist.class);
        songList = playlists.get(0).getSongList();
        Log.v("song", songList.get(0).getSongName());
        SongListAdapter adapter = new SongListAdapter(this,new SongListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Song song) {
                Intent intent = new Intent(PlaylistDetailActivity.this, PlayControlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Playlist", (ArrayList<? extends Parcelable>) songList);
                bundle.putInt("Position", songList.indexOf(song));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}