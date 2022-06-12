package ie.app.musicplayer.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ie.app.musicplayer.Activity.AlbumDetailActivity;
import ie.app.musicplayer.Activity.SearchActivity;
import ie.app.musicplayer.Adapter.SingerAdapter;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Model.Album;
import ie.app.musicplayer.Model.Singer;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class SearchSingerFragment extends Fragment {

    private List<Singer> singerList;
    private SingerAdapter singerAdapter;
    private RecyclerView singerRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_singer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        singerRecycleView = view.findViewById(R.id.singerView);
        singerAdapter = new SingerAdapter(getContext(), singer -> {
            Intent intent = new Intent(getContext(), AlbumDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Playlist", (ArrayList<? extends Parcelable>) singer.getSingerSong());
            bundle.putString("Name", singer.getSingerName());
            intent.putExtras(bundle);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        singerRecycleView.setLayoutManager(linearLayoutManager);

        singerRecycleView.setAdapter(singerAdapter);
    }

    public void updateSingerList(List<Singer> singerList) {
        singerAdapter.setData(singerList);
        this.singerList = singerList;
    }

    public void getFullSinger() {
        singerList = new ArrayList<>();
        Thread loading = new Thread(() -> {
            for (Map.Entry<String, ArrayList<Song>> entry : ((MusicPlayerApp) getActivity().getApplication()).singer.entrySet()) {
                singerList.add(new Singer(entry.getKey(), entry.getValue()));
            }
        });
        loading.run();
        singerAdapter.setData(singerList);
    }
}
