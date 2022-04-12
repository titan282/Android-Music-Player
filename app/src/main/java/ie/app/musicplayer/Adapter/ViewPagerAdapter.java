package ie.app.musicplayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ie.app.musicplayer.Fragment.PlaylistFragment;
import ie.app.musicplayer.Fragment.SongFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SongFragment();
            case 1:
                return new PlaylistFragment();
            default: return new SongFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
