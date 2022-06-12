package ie.app.musicplayer.Fragment;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
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
//        requestPermissionLauncher = registerForActivityResult(
//                new ActivityResultContracts.RequestPermission(), isGranted -> {
//                    if (isGranted) {
//                        loadSongFromSharedStorage();
//                        songListAdapter.setData(songList);
//                    } else {
//                        onRequestPermissionResult();
//                    }
//                });
//
//        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

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
                                return song.getSongName().compareTo(t1.getSongName());
                            }
                        });
                        songListAdapter.setData(temp);
                        songList=temp;
                        return true;
                    case R.id.descending:
                        Collections.sort(temp, new Comparator<Song>() {
                            @Override
                            public int compare(Song song, Song t1) {
                                return song.getSongName().compareTo(t1.getSongName());
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
        bundle.putParcelableArrayList("Playlist", (ArrayList<? extends Parcelable>) songList);
        Random random = new Random();
        int randomNumber = random.nextInt(songList.size()-1);
        bundle.putInt("Position",songList.indexOf(songList.get(randomNumber)));
        bundle.putBoolean("Random",true);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
    }

    private void openPlayer(Song song) {
        Intent intent = new Intent(SongFragment.this.getActivity(), PlayControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Playlist", (ArrayList<? extends Parcelable>) songList);
        bundle.putInt("Position", songList.indexOf(song));
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
    }



//    private void onRequestPermissionResult() {
//        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
//            loadSongFromSharedStorage();
//            songListAdapter.setData(songList);
//        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            // In an educational UI, explain to the user why your app requires this
//            // permission for a specific feature to behave as expected.
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
//            alertDialog.setTitle("Requesting Permission");
//            alertDialog.setMessage("Allow us to fetch songs form your device");
//
//            alertDialog.setPositiveButton("Allow", (dialogInterface, i) -> {
//                //request permission again
//                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            });
//
//            alertDialog.setNegativeButton("Not Allow", (dialogInterface, i) -> {
//                //request permission again
//                Toast.makeText(this.getContext(), "You denied to fetch songs", Toast.LENGTH_SHORT).show();
//                dialogInterface.dismiss();
//            });
//
//            alertDialog.show();
//        } else {
//            Toast.makeText(this.getContext(), "You denied to fetch songs", Toast.LENGTH_SHORT).show();
//            //We can close our application here
//        }
//    }

//    public void loadSongFromSharedStorage() {
//        if (((MusicPlayerApp)getActivity().getApplication()).songList != null &&
//                !(((MusicPlayerApp)getActivity().getApplication()).songList.isEmpty())) {
//            return;
//        }
//        songList = new ArrayList<>();
//        temp_album = new HashMap<>();
//        temp_singer = new HashMap<>();
//        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
//
//            String projection[] = new String[]{
//                    MediaStore.Audio.Media._ID,
//                    MediaStore.Audio.Media.TITLE,
//                    MediaStore.Audio.Media.ALBUM,
//                    MediaStore.Audio.Media.ARTIST,
//                    MediaStore.Audio.Media.DATA,
//                    MediaStore.Audio.Media.DATE_ADDED,
//            };
//            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//
//                loadingThread = new Thread(() -> {
//                    try (Cursor cursor = this.getContext().getApplicationContext()
//                            .getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                                    projection, null, null, sortOrder)) {
//                        while (cursor.moveToNext()) {
//                            int songId = cursor.getInt(0);
//                            String songName = cursor.getString(1);
//                            String songAlbum = cursor.getString(2);
//                            String songSinger = cursor.getString(3);
//                            String songURL = cursor.getString(4);
//                            String addedDate = cursor.getString(5);
//                            Song song = new Song(songId, songName, songAlbum, R.drawable.music_rect, songSinger, songURL, addedDate);
//                            songList.add(song);
//                        }
//                        ((MusicPlayerApp) getActivity().getApplication()).songList = new ArrayList<>(songList);
//                    }
//                });
//            loadingThread.start();
//            try {
//                loadingThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            Thread loadAlbumPicThread = new Thread(() -> {
//                for (Song song : songList) {
//                    song.loadEmbeddedPicture();
//                }
//            });
//            loadAlbumPicThread.start();
//
//            Thread loadAlbum = new Thread(() -> {
//                for (Song song : songList) {
//                    if (!temp_album.containsKey(song.getSongAlbum())) {
//                        temp_album.put(song.getSongAlbum(), new ArrayList<>());
//                    }
//                    temp_album.get(song.getSongAlbum()).add(song);
//
//                    if (!temp_singer.containsKey(song.getSongSinger())) {
//                        temp_singer.put(song.getSongSinger(), new ArrayList<>());
//                    }
//                    temp_singer.get(song.getSongSinger()).add(song);
//                }
//                ((MusicPlayerApp)getActivity().getApplication()).album = new HashMap<>(temp_album);
//                ((MusicPlayerApp)getActivity().getApplication()).singer = new HashMap<>(temp_singer);
//            });
//            loadAlbum.start();
//        }
//    }
}