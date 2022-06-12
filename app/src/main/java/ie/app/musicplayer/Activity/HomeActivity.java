package ie.app.musicplayer.Activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import ie.app.musicplayer.Adapter.ViewPagerAdapter;
import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Fragment.AlbumFragment;
import ie.app.musicplayer.Fragment.PlaylistFragment;
import ie.app.musicplayer.Fragment.SearchAlbumFragment;
import ie.app.musicplayer.Fragment.SearchSingerFragment;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class HomeActivity extends AppCompatActivity {

    private int tabPosition = 0;
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
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setText("Songs");
        mTabLayout.getTabAt(1).setText("Playlists");
        mTabLayout.getTabAt(2).setText("Albums");
        mTabLayout.getTabAt(3).setText("Artists");
        ivSort.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
            popupMenu.getMenuInflater().inflate(R.menu.option, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.refresh:
                            updateTab();
                    }
                return true;
                }
            });
        });
        mViewPager.setOffscreenPageLimit(adapter.getCount());

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                updateTab();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

    private void updateTab() {
        ViewPagerAdapter tempAdapter = (ViewPagerAdapter) mViewPager.getAdapter();
        Fragment tempFragment = tempAdapter.getItem(tabPosition);
        switch (tabPosition) {
            case 1:
                ((PlaylistFragment) tempFragment).updatePlaylist();
                break;
            case 2:
                ((SearchAlbumFragment) tempFragment).getFullAlbum();
                break;
            case 3:
                ((SearchSingerFragment) tempFragment).getFullSinger();
                break;
            default:

        }
    }
}