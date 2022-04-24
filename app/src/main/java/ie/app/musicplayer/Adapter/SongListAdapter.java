package ie.app.musicplayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

// Create Add Adapter Branch
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder>{

    private Context context;
    private List<Song> songList;

    public SongListAdapter(Context context) {
        this.context = context;
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

        holder.songImage.setImageResource(song.getSongImage());
        holder.songName.setText(song.getSongName());
        holder.songSinger.setText(song.getSongSinger());
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
//                popupMenu.setOnMenuItemClickListener();
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

    public class SongViewHolder extends RecyclerView.ViewHolder {

        private ImageView songImage;
        private TextView songName;
        private TextView songSinger;
        private ImageView menuMore;
        private ImageView songSave;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            songImage = itemView.findViewById(R.id.imageViewSongImage);
            songName = itemView.findViewById(R.id.textViewSongName);
            songSinger = itemView.findViewById(R.id.textViewSingerName);
            menuMore = itemView.findViewById(R.id.menuMore);
//            songSave = itemView.findViewById(R.id.imageViewtimdanhsachbaihat);
        }
    }

}
