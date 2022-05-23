package ie.app.musicplayer.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Adapter.ViewPagerAdapter;
import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class HomeActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageButton ibSearchBtn;
    private ImageView ivSort;
    private DBManager dbManager;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(HomeActivity.this);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        ivSort = findViewById(R.id.sortBtn);
        ibSearchBtn=findViewById(R.id.search_btn);

        ibSearchBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText("Songs");
        mTabLayout.getTabAt(1).setText("Playlists");
        mTabLayout.getTabAt(2).setText("Albums");
        mTabLayout.getTabAt(3).setText("Artists");
        ivSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.sort, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void addSongtoPlaylist(int i, Context context, Song song, Dialog dialog){
        List<Playlist> playlists = Playlist.listAll(Playlist.class);
        List<Song> songList = playlists.get(i).getSongList();
        if(checkSong(songList,song)){
            Toast.makeText(context,"This song has been added to "+playlists.get(i).getPlaylistName()+" playlist",Toast.LENGTH_SHORT).show();
            Log.v("song", "Add failed!");
        }
        else {
            playlists.get(i).getSongList().add(song);
            playlists.get(i).save();
            Toast.makeText(context,"Add song to "+playlists.get(i).getPlaylistName()+ " playlist successfully!",Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
        Log.v("song", "Add "+playlists.get(i).getPlaylistName()+ " "+song.getSongName());
        playlists = Playlist.listAll(Playlist.class);
        Log.v("song", "songList"+ playlists.get(i).getSongList().size());
    }

    private boolean checkSong(List<Song> songList, Song song) {
        List<String> songUrl = new ArrayList<String>();
        for(Song songItem:songList){
            songUrl.add(songItem.getSongURL());
        }
        return songUrl.contains(song.getSongURL());
    }
}