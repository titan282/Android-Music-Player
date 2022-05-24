package ie.app.musicplayer.Fragment;

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
import java.util.List;
import java.util.Map;

import ie.app.musicplayer.Activity.AlbumDetailActivity;
import ie.app.musicplayer.Adapter.AlbumAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Model.Album;
import ie.app.musicplayer.Model.Song;
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
            Intent intent = new Intent(getContext(), AlbumDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Playlist", (ArrayList<? extends Parcelable>) album.getAlbumData());
            bundle.putString("Name", album.getAlbumName());
            intent.putExtras(bundle);
            startActivity(intent);
            getFullAlbum();
            Log.v("Search Album Fragment", "Created");
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
        albumListAdapter.setData(albumList);
    }
}
