package ie.app.musicplayer.Fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Activity.PlayControlActivity;
import ie.app.musicplayer.Activity.SearchActivity;
import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class SearchSongFragment extends Fragment {

    private List<Song> songList = new ArrayList<>();
    private SongListAdapter songListAdapter;
    private RecyclerView songRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);
        View toolbar = view.findViewById(R.id.tool_bar_song);
        ((ViewGroup)toolbar.getParent()).removeView(toolbar);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.v("SearchSongFrag", "First");
        songRecycleView = view.findViewById(R.id.songView);
        songListAdapter = new SongListAdapter(getContext(), song -> {
            Intent intent = new Intent(SearchSongFragment.this.getActivity(), PlayControlActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Playlist", (ArrayList<? extends Parcelable>) songList);
            bundle.putInt("Position", songList.indexOf(song));
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        songRecycleView.setLayoutManager(linearLayoutManager);

        updateSongListAdapter(((MusicPlayerApp)getActivity().getApplication()).songList);
        songRecycleView.setAdapter(songListAdapter);
    }

    public void updateSongListAdapter(List<Song> songList) {
        Log.v("SearchSongFrag", "Second");
        songListAdapter.setData(songList);
        this.songList = songList;
    }
}
