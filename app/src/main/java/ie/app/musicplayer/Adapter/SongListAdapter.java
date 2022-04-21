package ie.app.musicplayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
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
        return new SongViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        if (song == null) {
            return;
        }

        holder.songImage.setImageResource(song.getSongImage());
        holder.songName.setText(song.getSongName());
        holder.songSinger.setText(song.getSongSinger());
        holder.layoutSongItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(song);
            }
        });
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
        private ImageView songSave;
        private ItemClickListener itemClickListener;
        private ConstraintLayout layoutSongItem;
        public SongViewHolder(@NonNull View itemView, ItemClickListener itemClickLister) {
            super(itemView);

            songImage = itemView.findViewById(R.id.imageViewSongImage);
            songName = itemView.findViewById(R.id.textViewSongName);
            songSinger = itemView.findViewById(R.id.textViewSingerName);
            songSave = itemView.findViewById(R.id.imageViewtimdanhsachbaihat);
            layoutSongItem = itemView.findViewById(R.id.layout_row_song);
        }


    }

    public interface ItemClickListener {
        void onItemClick(Song song);
    }

}
