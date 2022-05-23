package ie.app.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ie.app.musicplayer.Activity.PlaylistDetailActivity;
import ie.app.musicplayer.Dialog.AddToPlaylistDialog;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

// Create Add Adapter Branch
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder>{

    private Context context;
    private List<Song> songList;
    private ItemClickListener itemClickListener;
    public SongListAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<Song> list) {
        this.songList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        if (song == null) {
            return;
        }

        if (song.isHasPic()) {
           holder.songImage.setImageBitmap(song.getSongEmbeddedPicture());
        } else {
            holder.songImage.setImageResource(song.getSongImage());
        }
        holder.songName.setText(song.getSongName());
        holder.songSinger.setText(song.getSongSinger());
        holder.menuMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
            Menu menu = popupMenu.getMenu();
            MenuItem addItem = menu.findItem(R.id.AddToPlaylist);
            MenuItem removeItem = menu.findItem(R.id.removeSong);
            if(!context.getClass().equals(PlaylistDetailActivity.class)) {
                removeItem.setVisible(false);
            }
            else {
                addItem.setVisible(false);
            }
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.AddToPlaylist:
                            AddToPlaylistDialog addToPlaylistDialog = new AddToPlaylistDialog(context,song);
                            AppCompatActivity activity = (AppCompatActivity) context;
                            addToPlaylistDialog.show(activity.getSupportFragmentManager(), "My Manager");
                            break;

                        case R.id.removeSong:
                    }
                    return  true;
                }
            });

        });
        holder.layoutSongItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(song);
            }
        });
        holder.layoutSongItem.setOnClickListener(view -> itemClickListener.onItemClick(song));
    }

    @Override
    public int getItemCount() {
        if (songList != null) {
            return songList.size();
        }
        return 0;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{

        private ImageView songImage;
        private TextView songName;
        private TextView songSinger;
        private ImageView menuMore;
        private ImageView songSave;
        private ItemClickListener itemClickListener;
        private ConstraintLayout layoutSongItem;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            songImage = itemView.findViewById(R.id.albumImage);
            songName = itemView.findViewById(R.id.textViewSongName);
            songSinger = itemView.findViewById(R.id.textViewSingerName);
            menuMore = itemView.findViewById(R.id.menuMore);
//            songSave = itemView.findViewById(R.id.imageViewtimdanhsachbaihat);
            layoutSongItem = itemView.findViewById(R.id.layout_row_song);
        }


    }

    public interface ItemClickListener {
        void onItemClick(Song song);
    }

}
