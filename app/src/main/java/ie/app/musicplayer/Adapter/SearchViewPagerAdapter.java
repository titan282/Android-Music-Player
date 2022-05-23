package ie.app.musicplayer.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.HashMap;
import ie.app.musicplayer.Fragment.SearchAlbumFragment;
import ie.app.musicplayer.Fragment.SearchSingerFragment;
import ie.app.musicplayer.Fragment.SearchSongFragment;

public class SearchViewPagerAdapter extends FragmentPagerAdapter {

    private final HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();

    public SearchViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragmentHashMap.put(0, new SearchSongFragment());
        fragmentHashMap.put(1, new SearchAlbumFragment());
        fragmentHashMap.put(2, new SearchSingerFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentHashMap.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
