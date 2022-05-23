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
import java.util.List;
import ie.app.musicplayer.Model.Singer;
import ie.app.musicplayer.R;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.SingerViewHolder> {

    private final Context context;
    private final ItemClickListener itemClickListener;
    private List<Singer> singerList = new ArrayList<>();

    public interface ItemClickListener {
        void itemClickListener(Singer singer);
    }

    public void setData(List<Singer> singerList) {
        this.singerList = singerList;
        notifyDataSetChanged();
    }

    public SingerAdapter(Context context, SingerAdapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SingerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_singer, parent, false);
        return new SingerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingerViewHolder holder, int position) {
        Singer singer = singerList.get(position);

        holder.singerName.setText(singer.getSingerName());
        holder.singerImage.setImageResource(R.drawable.ic_artist);

        holder.layoutSingerItem.setOnClickListener(view -> {
            itemClickListener.itemClickListener(singer);
        });
    }

    @Override
    public int getItemCount() {
        if (singerList != null) {
            return singerList.size();
        }
        return 0;
    }

    public class SingerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView singerImage;
        private final TextView singerName;
        private final ConstraintLayout layoutSingerItem;

        public SingerViewHolder(@NonNull View itemView) {
            super(itemView);

            singerImage = itemView.findViewById(R.id.singerImage);
            singerName = itemView.findViewById(R.id.singerName);
            layoutSingerItem = itemView.findViewById(R.id.layout_singer_song);
        }
    }
}
