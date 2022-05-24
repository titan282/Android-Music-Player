package ie.app.musicplayer.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ie.app.musicplayer.Activity.HomeActivity;
import ie.app.musicplayer.Activity.PlayControlActivity;
import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class SongFragment extends Fragment {

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

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        loadSongFromSharedStorage();
                        songListAdapter.setData(songList);
                    } else {
                        onRequestPermissionResult();
                    }
                });

        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        songView.setLayoutManager(linearLayoutManager);

        songView.setAdapter(songListAdapter);

        return view;
    }

    private void openPlayer(Song song) {
        Intent intent = new Intent(SongFragment.this.getActivity(), PlayControlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Playlist", (ArrayList<? extends Parcelable>) songList);
        bundle.putInt("Position", songList.indexOf(song));
        intent.putExtras(bundle);
        startActivity(intent);
        sendNotificationMedia(song);
    }

    private void sendNotificationMedia(Song song) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this.getContext(),"tag");
        Notification notification = new NotificationCompat.Builder(this.getContext(), MusicPlayerApp.CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLargeIcon(song.getSongEmbeddedPicture())
                .setSubText("MusicPlayer")
                .setContentTitle(song.getSongName())
                .setSubText(song.getSongSinger())
                .setSmallIcon(R.drawable.ic_music)
                .addAction(R.drawable.ic_skip_previous, "Previous", null) // #0
                .addAction(R.drawable.ic_pause, "Pause", null)  // #1
                .addAction(R.drawable.ic_skip_next, "Next", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1 /* #1: pause button */)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this.getContext());
        Log.v("song", song.toString());
        managerCompat.notify(1,notification);
    }

    private void onRequestPermissionResult() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            loadSongFromSharedStorage();
            songListAdapter.setData(songList);
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected.
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
            alertDialog.setTitle("Requesting Permission");
            alertDialog.setMessage("Allow us to fetch songs form your device");

            alertDialog.setPositiveButton("Allow", (dialogInterface, i) -> {
                //request permission again
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            });

            alertDialog.setNegativeButton("Not Allow", (dialogInterface, i) -> {
                //request permission again
                Toast.makeText(this.getContext(), "You denied to fetch songs", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            });

            alertDialog.show();
        } else {
            Toast.makeText(this.getContext(), "You denied to fetch songs", Toast.LENGTH_SHORT).show();
            //We can close our application here
        }
    }

    public void loadSongFromSharedStorage() {
        if (((MusicPlayerApp)getActivity().getApplication()).songList != null &&
                !(((MusicPlayerApp)getActivity().getApplication()).songList.isEmpty())) {
            return;
        }
        songList = new ArrayList<>();
        temp_album = new HashMap<>();
        temp_singer = new HashMap<>();
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            String projection[] = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DATA,
            };
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

                loadingThread = new Thread(() -> {
                    try (Cursor cursor = this.getContext().getApplicationContext()
                            .getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                    projection, null, null, sortOrder)) {
                        while (cursor.moveToNext()) {
                            int songId = cursor.getInt(0);
                            String songName = cursor.getString(1);
                            String songAlbum = cursor.getString(2);
                            String songSinger = cursor.getString(3);
                            String songURL = cursor.getString(4);

                            Song song = new Song(songId, songName, songAlbum, R.drawable.music_rect, songSinger, songURL);
                            songList.add(song);
                        }
                        ((MusicPlayerApp) getActivity().getApplication()).songList = new ArrayList<>(songList);
                    }
                });
            loadingThread.start();
            try {
                loadingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread loadAlbumPicThread = new Thread(() -> {
                for (Song song : songList) {
                    song.loadEmbeddedPicture();
                }
            });
            loadAlbumPicThread.start();

            Thread loadAlbum = new Thread(() -> {
                for (Song song : songList) {
                    if (!temp_album.containsKey(song.getSongAlbum())) {
                        temp_album.put(song.getSongAlbum(), new ArrayList<>());
                    }
                    temp_album.get(song.getSongAlbum()).add(song);

                    if (!temp_singer.containsKey(song.getSongSinger())) {
                        temp_singer.put(song.getSongSinger(), new ArrayList<>());
                    }
                    temp_singer.get(song.getSongSinger()).add(song);
                }
                ((MusicPlayerApp)getActivity().getApplication()).album = new HashMap<>(temp_album);
                ((MusicPlayerApp)getActivity().getApplication()).singer = new HashMap<>(temp_singer);
            });
            loadAlbum.start();
        }
    }
}