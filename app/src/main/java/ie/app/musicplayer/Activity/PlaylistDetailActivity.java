package ie.app.musicplayer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;
import ie.app.musicplayer.Utility.Constant;

public class PlaylistDetailActivity extends AppCompatActivity {

    RecyclerView rvSongList;
    ImageView ivPlaylistCover;
    ImageButton ibBackBtn;
    int playlistCover;
    int playlistId;
    TextView tvPlaylistName;
    List<Song> songList= new ArrayList<>();
    List<Playlist> playlists ;
    SongListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlists = Playlist.listAll(Playlist.class);
        Intent intent = getIntent();
        playlistId= intent.getExtras().getInt(Constant.POSITION_KEY);
        songList = playlists.get(playlistId).getSongList();
        playlistCover = intent.getExtras().getInt(Constant.COVER_KEY);
        setContentView(R.layout.activity_playlist_detail);
        rvSongList = findViewById(R.id.playlistDetailRecycleView);
        ivPlaylistCover = findViewById(R.id.playlistCover);
        ivPlaylistCover.setImageResource(playlistCover);
        ibBackBtn = findViewById(R.id.backBtnPlaylistDetail);
        ibBackBtn.setOnClickListener(view -> {
            onBackPressed();
        });
        tvPlaylistName = findViewById(R.id.playlistNamePlaylistDetail);
        tvPlaylistName.setText(playlists.get(playlistId).getPlaylistName());
        Log.v("song",playlists.get(playlistId).getPlaylistName()+ songList.size());
        adapter = new SongListAdapter(PlaylistDetailActivity.this,new SongListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Song song) {
                Intent intent = new Intent(PlaylistDetailActivity.this, PlayControlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constant.PLAYLIST_KEY, (ArrayList<? extends Parcelable>) songList);
                bundle.putInt(Constant.POSITION_KEY, songList.indexOf(song));
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            }
        },playlistId);

            for (Song song : songList) {
                if (song.isHasPic()) {
                    song.loadEmbeddedPicture();
                }
            }

        adapter.setData(songList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvSongList.setLayoutManager(linearLayoutManager);
        rvSongList.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Debug","Result PlaylistDetailActivity");
//        adapter.update();
    }


}