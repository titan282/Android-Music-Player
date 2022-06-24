package ie.app.musicplayer.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ie.app.musicplayer.Activity.AlbumDetailActivity;
import ie.app.musicplayer.Adapter.AlbumAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Model.Album;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;
import ie.app.musicplayer.Utility.Constant;

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
            Intent intent = new Intent(getContext(), AlbumDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(Constant.PLAYLIST_KEY, (ArrayList<? extends Parcelable>) album.getAlbumData());
            bundle.putString(Constant.NAME_KEY, album.getAlbumName());
            intent.putExtras(bundle);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            getFullAlbum();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        albumRecycleView.setLayoutManager(linearLayoutManager);

        albumRecycleView.setAdapter(albumListAdapter);
    }

    public void updateAlbumList(List<Album> albumList) {
        albumListAdapter.setData(albumList);
        this.albumList = albumList;
    }

    public void getFullAlbum() {
        albumList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Song>> entry : ((MusicPlayerApp) getActivity().getApplication()).album.entrySet()) {
                albumList.add(new Album(entry.getKey(), entry.getValue()));
        }
        Collections.sort(albumList, new Comparator<Album>() {
            @Override
            public int compare(Album album, Album t1) {
                return album.getAlbumName().compareTo(t1.getAlbumName());
            }
        });
        albumListAdapter.setData(albumList);
    }
}
