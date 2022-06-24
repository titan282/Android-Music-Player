package ie.app.musicplayer.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import ie.app.musicplayer.Activity.PlayControlActivity;
import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;
import ie.app.musicplayer.Utility.Constant;

public class SongFragment extends Fragment{

    private View view;
    private RecyclerView songView;
    private List<Song> songList;
    private SongListAdapter songListAdapter;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Thread loadingThread;
    private HashMap<String, ArrayList<Song>> temp_album = new HashMap<>();
    private HashMap<String, ArrayList<Song>> temp_singer = new HashMap<>();

    public MusicPlayerApp app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app =(MusicPlayerApp) getActivity().getApplication();
        view = inflater.inflate(R.layout.fragment_song, container, false);
        songView = view.findViewById(R.id.songView);
        songListAdapter = new SongListAdapter(getContext(), song -> openPlayer(song));
        songList = app.songList;
        songListAdapter.setData(songList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        songView.setLayoutManager(linearLayoutManager);

        songView.setAdapter(songListAdapter);

        ((ImageButton) view.findViewById(R.id.sortBtn)).setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this.getContext(), view);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                List<Song> temp = new ArrayList<>(songList);
                switch (menuItem.getItemId()) {
                    case R.id.ascending:
                        Collections.sort(temp, new Comparator<Song>() {
                            @Override
                            public int compare(Song song, Song t1) {
                                return Constant.sorting.generator(song.getSongName()).compareTo(
                                        Constant.sorting.generator(t1.getSongName()));
                            }
                        });
                        songListAdapter.setData(temp);
                        songList=temp;
                        return true;
                    case R.id.descending:
                        Collections.sort(temp, new Comparator<Song>() {
                            @Override
                            public int compare(Song song, Song t1) {
                                return Constant.sorting.generator(song.getSongName()).compareTo(
                                        Constant.sorting.generator(t1.getSongName()));
                            }
                        });
                        Collections.reverse(temp);
                        songList=temp;
                        songListAdapter.setData(temp);
                        return true;
                    case R.id.date_added:
                        Collections.sort(temp, new Comparator<Song>() {
                            @Override
                            public int compare(Song song, Song t1) {
                                return song.getAddedDate().compareTo(t1.getAddedDate());
                            }
                        });
                        Collections.reverse(temp);
                        songList=temp;
                        songListAdapter.setData(temp);
                        return true;
                    default:
                      return false;
                }
            });
            popupMenu.inflate(R.menu.sort);
            popupMenu.show();
        });
        ((MaterialButton) view.findViewById(R.id.randomBtn)).setOnClickListener(view -> {
            openRandomPlayer();
        });
        ((MaterialButton) view.findViewById(R.id.playall)).setOnClickListener(view ->{
            openPlayer(songList.get(0));
        });
        return view;
    }

    private void openRandomPlayer() {
        Intent intent = new Intent(SongFragment.this.getActivity(), PlayControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.PLAYLIST_KEY, (ArrayList<? extends Parcelable>) songList);
        Random random = new Random();
        int randomNumber = random.nextInt(songList.size()-1);
        bundle.putInt(Constant.POSITION_KEY,songList.indexOf(songList.get(randomNumber)));
        bundle.putBoolean(Constant.RANDOM_KEY,true);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
    }

    private void openPlayer(Song song) {
        Intent intent = new Intent(SongFragment.this.getActivity(), PlayControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.PLAYLIST_KEY, (ArrayList<? extends Parcelable>) songList);
        bundle.putInt(Constant.POSITION_KEY, songList.indexOf(song));
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
    }
}