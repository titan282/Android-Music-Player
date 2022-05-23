package ie.app.musicplayer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageButton;
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
    private ImageButton addBtn, playBtn;
    private TextView albumName;
    private List<Song> albumData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        albumData = (List<Song>) getIntent().getExtras().get("Playlist");
        albumName = findViewById(R.id.albumName);
        albumName.setText((String) getIntent().getExtras().get("Name"));

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
    }

}
