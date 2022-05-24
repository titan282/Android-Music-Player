package ie.app.musicplayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.HashMap;
import ie.app.musicplayer.Fragment.SearchAlbumFragment;
import ie.app.musicplayer.Fragment.SearchSingerFragment;
import ie.app.musicplayer.Fragment.SearchSongFragment;

public class SearchViewPagerAdapter extends FragmentPagerAdapter {

    private final HashMap<Integer, Fragment> searchFragmentHashMap = new HashMap<>();

    public SearchViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        searchFragmentHashMap.put(0, new SearchSongFragment());
        searchFragmentHashMap.put(1, new SearchAlbumFragment());
        searchFragmentHashMap.put(2, new SearchSingerFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return searchFragmentHashMap.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
