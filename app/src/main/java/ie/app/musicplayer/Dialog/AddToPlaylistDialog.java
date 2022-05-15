package ie.app.musicplayer.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

import ie.app.musicplayer.Adapter.PlaylistDialogAdapter;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class AddToPlaylistDialog extends AppCompatDialogFragment {
    private Context context;
    private Song song;
    private List<Playlist> playlists;
    public AddToPlaylistDialog(Context context, Song song){
        this.context = context;
        this.song =song;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_add_playlist,container,false);
        PlaylistDialogAdapter adapter = new PlaylistDialogAdapter(context);
        ListView lvPlaylistName = view.findViewById(R.id.lvPlaylistName);
        lvPlaylistName.setAdapter(adapter);
        LinearLayout newPlaylist = view.findViewById(R.id.newPlaylist);
        newPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                dismiss();
                View view1 = getActivity().getLayoutInflater().inflate(R.layout.dialog_playlist_name,null);
                EditText playlistName = view1.findViewById(R.id.etPlaylistName);
                builder.setView(view1)
                        .setTitle("Add Playlist")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<Song> songList = new ArrayList<Song>();
                                songList.add(song);
                                new Playlist(playlistName.getText().toString(),R.drawable.music_rect, songList).save();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }
        });
        lvPlaylistName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playlists = Playlist.listAll(Playlist.class);
//                Playlist.deleteAll(Playlist.class);
//                playlists.get(0).addToSongList(song);
                Log.v("playlist", playlists.get(0).getPlaylistName()+ " "+playlists.get(0).getSongList().get(0).getSongName());
            }
        });
        return view;

    }



}
