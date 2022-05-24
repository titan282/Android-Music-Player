package ie.app.musicplayer.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ie.app.musicplayer.Adapter.SearchViewPagerAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Fragment.SearchAlbumFragment;
import ie.app.musicplayer.Fragment.SearchSingerFragment;
import ie.app.musicplayer.Fragment.SearchSongFragment;
import ie.app.musicplayer.Model.Album;
import ie.app.musicplayer.Model.Singer;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class SearchActivity extends AppCompatActivity {

    private TabLayout sTabLayout;
    private ViewPager sViewPager;
    private DBManager dbManager;
    private EditText sEditText;
    private int currentTab;

    private List<Song> songList = new ArrayList<>();
    private List<Album> albumList = new ArrayList<>();
    private List<Singer> singerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        sTabLayout = findViewById(R.id.sTabLayout);
        sViewPager = findViewById(R.id.sViewPager);
        sEditText = findViewById(R.id.sTextView);
        sEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searching();
            }
        });


        SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sViewPager.setAdapter(searchViewPagerAdapter);
        sTabLayout.setupWithViewPager(sViewPager);
        sTabLayout.getTabAt(0).setText("Song");
        sTabLayout.getTabAt(1).setText("Album");
        sTabLayout.getTabAt(2).setText("Singer");
        sTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                currentTab = tab.getPosition();
                searching();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void searching() {
        songList.clear();
        albumList.clear();
        singerList.clear();

        Thread searchingSongThread = new Thread(() -> {
            for (Song song : ((MusicPlayerApp) getApplication()).songList) {
                if (song.getSongName().toLowerCase().contains(String.valueOf(sEditText.getText()))) {
                    songList.add(song);
                }
            }
        });

        Thread searchingAlbumThread = new Thread(() -> {
            for (Map.Entry<String, ArrayList<Song>> entry : ((MusicPlayerApp) getApplication()).album.entrySet()) {
                if (entry.getKey().toLowerCase().contains(sEditText.getText())) {
                    albumList.add(new Album(entry.getKey(), entry.getValue()));
                }
            }
        });

        Thread searchingSingerThread = new Thread(() -> {
            for (Map.Entry<String, ArrayList<Song>> entry : ((MusicPlayerApp) getApplication()).singer.entrySet()) {
                if (entry.getKey().toLowerCase().contains(sEditText.getText())) {
                    singerList.add(new Singer(entry.getKey(), entry.getValue()));
                }
            }
        });

        searchingSingerThread.start();
        searchingAlbumThread.start();
        searchingSongThread.start();

        switch (currentTab) {
            case 0: updateSongFragment();
                break;
            case 1: updateAlbumFragment();
                break;
            case 2: updateSingerFragment();
                break;
        }
    }

    public void updateSongFragment() {
        SearchSongFragment searchSongFragment = (SearchSongFragment) ((SearchViewPagerAdapter) Objects.requireNonNull(
                sViewPager.getAdapter())).getItem(0);
        searchSongFragment.updateSongListAdapter(songList);
    }

    public void updateAlbumFragment() {
        SearchAlbumFragment searchAlbumFragment = (SearchAlbumFragment) ((SearchViewPagerAdapter)
                sViewPager.getAdapter()).getItem(1);
        searchAlbumFragment.updateAlbumList(albumList);
    }

    public void updateSingerFragment() {
        SearchSingerFragment searchSingerFragment = (SearchSingerFragment) ((SearchViewPagerAdapter)
                sViewPager.getAdapter()).getItem(2);
        searchSingerFragment.updateSingerList(singerList);
    }
}
