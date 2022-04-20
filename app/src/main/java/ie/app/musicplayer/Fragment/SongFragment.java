package ie.app.musicplayer.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class SongFragment extends Fragment {

    private View view;
    private RecyclerView songView;
    private SongListAdapter songListAdapter;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_song, container, false);
        songView = view.findViewById(R.id.songView);
        songListAdapter = new SongListAdapter(getContext());

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        songListAdapter.setData(loadSongFromSharedStorage());
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

    private void onRequestPermissionResult() {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            songListAdapter.setData(loadSongFromSharedStorage());
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
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

    public List<Song> loadSongFromSharedStorage() {
        List<Song> songList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            String projection[] = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA
            };
            String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

            try (Cursor cursor = this.getContext().getApplicationContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, sortOrder)) {
                while (cursor.moveToNext()) {
                    Log.d("Permission", "loadSong");
                    int songId = cursor.getInt(0);
                    String songName = cursor.getString(1);
                    String songAlbum = cursor.getString(2);
                    String songSinger = cursor.getString(3);
                    int songDuration = cursor.getInt(4);
                    String songURL = cursor.getString(5);

                    songList.add(new Song(songId, songDuration, songName, songAlbum, R.drawable.music_rect, songSinger, songURL));
                }
            }
        }
        return songList;
    }
}