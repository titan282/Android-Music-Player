package ie.app.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.app.musicplayer.Model.Album;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private Context context;
    private ItemClickListener itemClickListener;
    private List<Album> albumList = new ArrayList<>();

    public interface ItemClickListener {
        void itemClickListener(Album album);
    }

    public void setData(List<Album> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }

    public AlbumAdapter(Context context, AlbumAdapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);

        holder.albumName.setText(album.getAlbumName());
        if (album.getAlbumData().get(0).isHasPic()) {
            holder.albumImage.setImageBitmap(album.getAlbumData().get(0).getSongEmbeddedPicture());
        } else {
            holder.albumImage.setImageResource(R.drawable.ic_album);
        }

        holder.layoutAlbumItem.setOnClickListener(view -> {
            itemClickListener.itemClickListener(album);
        });
    }

    @Override
    public int getItemCount() {
        if (albumList != null) {
            return albumList.size();
        }
        return 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumImage;
        private TextView albumName;
        private ConstraintLayout layoutAlbumItem;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            albumImage = itemView.findViewById(R.id.albumImage);
            albumName = itemView.findViewById(R.id.albumName);
            layoutAlbumItem = itemView.findViewById(R.id.layout_album_song);
        }
    }
}
