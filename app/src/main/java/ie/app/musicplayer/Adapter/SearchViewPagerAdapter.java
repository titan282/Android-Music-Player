package ie.app.musicplayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Objects;

import ie.app.musicplayer.Fragment.SearchSingerFragment;
import ie.app.musicplayer.Fragment.SearchSongFragment;

public class SearchViewPagerAdapter extends FragmentPagerAdapter {

    private final HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();

    public SearchViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragmentHashMap.put(0, new SearchSongFragment());
        fragmentHashMap.put(1, new SearchSingerFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return Objects.requireNonNull(fragmentHashMap.get(position));
    }

    @Override
    public int getCount() {
        return 2;
    }
}
