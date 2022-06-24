package ie.app.musicplayer.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class SplashActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app =(MusicPlayerApp)getApplication();
        setContentView(R.layout.activity_splash);
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        loadSongFromSharedStorage();
                    } else {
                        onRequestPermissionResult();
                    }
                });

        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
            }
        },2000);
    }
    public void loadSongFromSharedStorage() {
        if (((MusicPlayerApp)getApplication()).songList != null &&
                !(((MusicPlayerApp)getApplication())).songList.isEmpty()) {
            return;
        }
        songList = new ArrayList<>();
        temp_album = new HashMap<>();
        temp_singer = new HashMap<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            String projection[] = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DATE_ADDED,
            };
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

            loadingThread = new Thread(() -> {
                try (Cursor cursor = getApplicationContext()
                        .getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                projection, null, null, sortOrder)) {
                    while (cursor.moveToNext()) {
                        int songId = cursor.getInt(0);
                        String songName = cursor.getString(1);
                        String songAlbum = cursor.getString(2);
                        String songSinger = cursor.getString(3);
                        String songURL = cursor.getString(4);
                        String addedDate = cursor.getString(5);
                        Song song = new Song(songId, songName, songAlbum, R.drawable.music_rect, songSinger, songURL, addedDate);
                        songList.add(song);
                    }
                    ((MusicPlayerApp)getApplication()).songList = new ArrayList<>(songList);
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
                ((MusicPlayerApp)getApplication()).album = new HashMap<>(temp_album);
                ((MusicPlayerApp)getApplication()).singer = new HashMap<>(temp_singer);
            });
            loadAlbum.start();
        }
    }
    private void onRequestPermissionResult() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            loadSongFromSharedStorage();
            songListAdapter.setData(songList);
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected.
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Requesting Permission");
            alertDialog.setMessage("Allow us to fetch songs form your device");

            alertDialog.setPositiveButton("Allow", (dialogInterface, i) -> {
                //request permission again
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            });

            alertDialog.setNegativeButton("Not Allow", (dialogInterface, i) -> {
                //request permission again
                Toast.makeText(this, "You denied to fetch songs", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            });

            alertDialog.show();
        } else {
            Toast.makeText(this, "You denied to fetch songs", Toast.LENGTH_SHORT).show();
            //We can close our application here
        }
    }
}