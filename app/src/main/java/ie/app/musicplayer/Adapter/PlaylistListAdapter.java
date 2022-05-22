package ie.app.musicplayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ie.app.musicplayer.Dialog.AddToPlaylistDialog;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Activity.PlaylistDetailActivity;
import ie.app.musicplayer.R;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder> {
    public static final String POSITION = "position";
    private Context context;
    private List<Playlist> playlists;

    public PlaylistListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Playlist> playlists) {
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Playlist playlist = playlists.get(position);
        if (playlist == null) return;
        holder.playListName.setText(playlist.getPlaylistName());
        holder.playListImg.setImageResource(playlist.getPlaylistImage());
        holder.tvNumSong.setText(playlists.get(position).getSongList().size()+" songs");
        holder.ibMenuPlaylist.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.playlist_option, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.deletePlaylist:
                            if(position!=0) {
                                deletePlaylist(position);
                            }
                            else {
                                Toast.makeText(context,"Can't delete Favorites Playlist!",Toast.LENGTH_SHORT).show();
                            }
                    }
                    return  true;
                }
            });
        });
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlaylistDetailActivity.class);
            intent.putExtra(POSITION,position);
            intent.putExtra("cover",playlists.get(position).getPlaylistImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return playlists == null ? 0 : playlists.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private ImageView playListImg;
        private TextView playListName;
        private ImageButton ibMenuPlaylist;
        private TextView tvNumSong;
        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playListImg = itemView.findViewById(R.id.imageviewplaylist);
            playListName = itemView.findViewById(R.id.textviewplaylist);
            ibMenuPlaylist = itemView.findViewById(R.id.menuPlaylist);
            tvNumSong = itemView.findViewById(R.id.numSong);
        }
    }

    public void deletePlaylist(int positon){
        List<Playlist> playlists = Playlist.listAll(Playlist.class);
        Playlist playlist = playlists.get(positon);
        if(playlist.delete()){
            Toast.makeText(context,"Delete successfully!",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Delete failed!",Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
    }
}
