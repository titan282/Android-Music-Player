package ie.app.musicplayer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import ie.app.musicplayer.Utility.Constant;

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

        albumData = (List<Song>) getIntent().getExtras().get(Constant.PLAYLIST_KEY);
        albumName = findViewById(R.id.albumName);
        albumName.setText((String) getIntent().getExtras().get(Constant.NAME_KEY));
        ivAlbumCover = findViewById(R.id.albumCover);
        backBtn = findViewById(R.id.backButton);
        for(Song song:albumData){
            if (song.isHasPic()) {
                song.loadEmbeddedPicture();
            }
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
            bundle.putParcelableArrayList(Constant.PLAYLIST_KEY, (ArrayList<? extends Parcelable>) albumData);
            bundle.putInt(Constant.POSITION_KEY, albumData.indexOf(song));
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
        });

        albumAdapter.setData(albumData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AlbumDetailActivity.this);
        albumReCycleView.setLayoutManager(linearLayoutManager);
        albumReCycleView.setAdapter(albumAdapter);
        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
