package ie.app.musicplayer.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ie.app.musicplayer.Activity.SearchActivity;
import ie.app.musicplayer.Adapter.AlbumAdapter;
import ie.app.musicplayer.Model.Album;
import ie.app.musicplayer.R;

public class SearchAlbumFragment extends Fragment {

    private List<Album> albumList;
    private AlbumAdapter albumListAdapter;
    private RecyclerView albumRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        albumRecycleView = view.findViewById(R.id.albumView);
        albumListAdapter = new AlbumAdapter(getContext(), album -> {

        });
        ((SearchActivity) getActivity()).updateAlbumFragment();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        albumRecycleView.setLayoutManager(linearLayoutManager);

        albumRecycleView.setAdapter(albumListAdapter);
    }

    public void updateAlbumList(List<Album> albumList) {
        albumListAdapter.setData(albumList);
        this.albumList = albumList;
    }
}
