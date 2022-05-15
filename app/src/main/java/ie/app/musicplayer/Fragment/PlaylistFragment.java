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
        dbManager = new DBManager(getContext());
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistReCycleView = view.findViewById(R.id.playlistRecycleView);
        playlistListAdapter = new PlaylistListAdapter(this.getContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        playlistReCycleView.setLayoutManager(gridLayoutManager);

        // List for testing

//        playlists.get(0).save();
//        playlists.add(new Playlist(2, "Pop", R.drawable.music_rect, new ArrayList<Song>()));
//        playlists.get(1).save();
        playlistListAdapter.setData(Playlist.listAll(Playlist.class));
        playlistReCycleView.setAdapter(playlistListAdapter);
        playlistListAdapter.notifyDataSetChanged();

    }
}
