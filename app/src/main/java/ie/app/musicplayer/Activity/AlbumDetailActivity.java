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
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class AlbumDetailActivity extends AppCompatActivity {

    private RecyclerView albumReCycleView;
    private SongListAdapter albumAdapter;
    private ImageButton addBtn, playBtn,backBtn;
    private TextView albumName;
    private List<Song> albumData;
    private ImageView ivAlbumCover;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        albumData = (List<Song>) getIntent().getExtras().get("Playlist");
        albumName = findViewById(R.id.albumName);
        albumName.setText((String) getIntent().getExtras().get("Name"));
        ivAlbumCover = findViewById(R.id.albumCover);
        backBtn = findViewById(R.id.backButton);
        for(Song song:albumData){
            song.loadEmbeddedPicture();
        }
        if (albumData.get(0).isHasPic()) {
            ivAlbumCover.setImageBitmap(albumData.get(0).getSongEmbeddedPicture());
        } else {
            ivAlbumCover.setImageResource(R.drawable.ic_album);
        }
        albumReCycleView = findViewById(R.id.songAlbumList);
        albumAdapter = new SongListAdapter(AlbumDetailActivity.this, song -> {
            Intent intent = new Intent(AlbumDetailActivity.this, PlayControlActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Playlist", (ArrayList<? extends Parcelable>) albumData);
            bundle.putInt("Position", albumData.indexOf(song));
            intent.putExtras(bundle);
            startActivity(intent);
        });

        albumAdapter.setData(albumData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AlbumDetailActivity.this);
        albumReCycleView.setLayoutManager(linearLayoutManager);
        albumReCycleView.setAdapter(albumAdapter);
        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });
    }

}
