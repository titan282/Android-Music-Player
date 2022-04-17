package ie.app.musicplayer.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class SongFragment extends Fragment {

    private View view;
    private RecyclerView songView;
    private SongListAdapter songListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_song, container, false);
        songView = view.findViewById(R.id.songView);
        songListAdapter = new SongListAdapter(getContext());

        Log.e("c", "check");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        songView.setLayoutManager(linearLayoutManager);

        songListAdapter.setData(getListSong());

        songView.setAdapter(songListAdapter);

        return view;
    }

    private List<Song> getListSong() {
        List<Song> list = new ArrayList<>();
        list.add(new Song(1, "Never Gonna Give You Up", R.drawable.music_rect,
                "Rick Ashley", null));
        list.add(new Song(2, "Never Gonna Give You Up 2.0", R.drawable.music_rect,
                "Rick Ashley", null));
        list.add(new Song(3, "Never Gonna Give You Up 3.0", R.drawable.music_rect,
                "Rick Ashley", null));
        list.add(new Song(4, "Never Gonna Give You Up 4.0", R.drawable.music_rect,
                "Rick Ashley", null));

        return list;
    }
}