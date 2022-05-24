package ie.app.musicplayer.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Adapter.PlaylistListAdapter;
import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class PlaylistFragment extends Fragment {

    private RecyclerView playlistReCycleView;
    private PlaylistListAdapter playlistListAdapter;
    private DBManager dbManager;
    private List<Playlist> playlists = new ArrayList<Playlist>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playlistReCycleView = view.findViewById(R.id.playlistRecycleView);
        playlistListAdapter = new PlaylistListAdapter(this.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        playlistReCycleView.setLayoutManager(gridLayoutManager);
        playlistReCycleView.setAdapter(playlistListAdapter);
    }

    public void updatePlaylist() {
        playlistListAdapter.setData(Playlist.listAll(Playlist.class));
    }
}
