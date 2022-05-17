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
import java.util.Objects;

import ie.app.musicplayer.Adapter.SearchViewPagerAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Fragment.SearchSongFragment;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class SearchActivity extends AppCompatActivity {

    private TabLayout sTabLayout;
    private ViewPager sViewPager;
    private DBManager dbManager;
    private EditText sEditText;

    private List<Song> songList = new ArrayList<>();

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
                Log.v("Searching Activity", "afterTextChanged");
            }
        });


        SearchViewPagerAdapter searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sViewPager.setAdapter(searchViewPagerAdapter);
        sTabLayout.setupWithViewPager(sViewPager);
        sTabLayout.getTabAt(0).setText("Songs");
//        sTabLayout.getTabAt(1).setText("Playlists");
//        sTabLayout.getTabAt(2).setText("Albums");
        sTabLayout.getTabAt(1).setText("Artists");
    }

    private void searching() {
        if (songList != null) {
            songList.clear();
        }

        Log.v("Searching Activity", String.valueOf(sEditText.getText()));

        Thread searchingThread = new Thread(() -> {
            List<Song> tempList = new ArrayList<>(((MusicPlayerApp) getApplication()).songList);
            Log.v("Searching Activity", String.valueOf(tempList.isEmpty()));
            for (Song song : tempList) {
                if (song.getSongName().contains(String.valueOf(sEditText.getText()))) {
                    songList.add(song);
                }
            }
        });

        searchingThread.start();
        updateSongFragment();
    }

    public void updateSongFragment() {
        SearchSongFragment searchSongFragment = (SearchSongFragment) ((SearchViewPagerAdapter) Objects.requireNonNull(
                sViewPager.getAdapter())).getItem(0);
        searchSongFragment.updateSongListAdapter(songList);
    }
}
