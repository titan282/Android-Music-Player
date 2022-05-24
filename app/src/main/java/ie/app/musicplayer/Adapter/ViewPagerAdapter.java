package ie.app.musicplayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.HashMap;
import java.util.Objects;
import ie.app.musicplayer.Fragment.PlaylistFragment;
import ie.app.musicplayer.Fragment.SearchAlbumFragment;
import ie.app.musicplayer.Fragment.SearchSingerFragment;
import ie.app.musicplayer.Fragment.SongFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final HashMap<Integer, Fragment> mainFragmentHashmap = new HashMap<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mainFragmentHashmap.put(0, new SongFragment());
        mainFragmentHashmap.put(1, new PlaylistFragment());
        mainFragmentHashmap.put(2, new SearchAlbumFragment());
        mainFragmentHashmap.put(3, new SearchSingerFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return Objects.requireNonNull(mainFragmentHashmap.get(position));
    }

    @Override
    public int getCount() {
        return mainFragmentHashmap.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
