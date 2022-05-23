package ie.app.musicplayer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.R;

public class PlaylistDialogAdapter extends BaseAdapter {
    private Context context;
    private List<Playlist> playlists;
    public PlaylistDialogAdapter(Context context){
        this.context=context;
        playlists = Playlist.listAll(Playlist.class);
    }
    @Override
    public int getCount() {
        return playlists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        view = layoutInflater.inflate(R.layout.item_playlist_name,null);
        TextView tvPlaylistName = view.findViewById(R.id.playlistName);
        ImageView ivPlaylistCover = view.findViewById(R.id.playlistCover);
        ivPlaylistCover.setImageResource(playlists.get(i).getPlaylistImage());
        tvPlaylistName.setText(playlists.get(i).getPlaylistName());

        return view;
    }
}
