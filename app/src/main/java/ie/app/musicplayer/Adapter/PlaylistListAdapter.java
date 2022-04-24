package ie.app.musicplayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.PlaylistDetail;
import ie.app.musicplayer.R;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder> {

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
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        if (playlist == null) return;
        holder.playListName.setText(playlist.getPlaylistName());
        holder.playListImg.setImageResource(playlist.getPlaylistImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlaylistDetail.class);
//                intent.putExtra("PlaylistName", );
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists == null ? 0 : playlists.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

        private ImageView playListImg;
        private TextView playListName;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            playListImg = itemView.findViewById(R.id.imageviewplaylist);
            playListName = itemView.findViewById(R.id.textviewplaylist);
        }
    }
}
