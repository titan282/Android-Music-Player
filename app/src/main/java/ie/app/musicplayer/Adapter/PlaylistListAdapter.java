package ie.app.musicplayer.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Activity.PlaylistDetailActivity;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder> {
    public static final String POSITION = "position";
    private Context context;
    private List<Playlist> playlists;
    private ItemClickListener itemClickListener;
    private ConstraintLayout layoutPlaylistFramgment;
    public PlaylistListAdapter(Context context) {
        this.context = context;
    }
    public PlaylistListAdapter(Context context, ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
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
                                Toast.makeText(context,"Không thể xóa Favorite Playlist!",Toast.LENGTH_SHORT).show();
                            }
                            break;
//                        case R.id.deleteAll:
//                            deleteAllSong(position);
                    }
                    return  true;
                }
            });
        });
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlaylistDetailActivity.class);
            intent.putExtra(POSITION,position);
            intent.putExtra("cover",playlists.get(position).getPlaylistImage());
            ((Activity)context).startActivityForResult(intent,1, ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
        });

    }

    private void deleteAllSong(int position) {
        List<Playlist> playlists = Playlist.listAll(Playlist.class);
        Playlist playlist = playlists.get(position);
        playlist.getSongList().clear();
        playlist.save();
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
            layoutPlaylistFramgment = itemView.findViewById(R.id.layoutPlaylistFragment);
        }
    }

    public void deletePlaylist(int positon){
        List<Playlist> playlists = Playlist.listAll(Playlist.class);
        Playlist playlist = playlists.get(positon);
        if(playlist.delete()){
            Toast.makeText(context,"Xóa thành công!",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context,"Xóa thất bại!",Toast.LENGTH_SHORT).show();
        }
        update(playlists);
    }
    public void update(@NonNull List<Playlist> playlists){
        playlists.clear();
        setData(Playlist.listAll(Playlist.class));
    }
    public interface ItemClickListener {
        void onItemClick(Song song);
    }
}
