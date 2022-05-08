package ie.app.musicplayer.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class PlayControlBottomSheetFragment extends BottomSheetDialogFragment {

    private List<Song> songList;

    public PlayControlBottomSheetFragment(List<Song> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_fragment_play_control, null);
        bottomSheetDialog.setContentView(view);

//        ((ImageButton)view.findViewById(R.id.closeButton)).setOnClickListener(view1 -> bottomSheetDialog.dismiss());

        RecyclerView recyclerViewData = view.findViewById(R.id.songView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewData.setLayoutManager(linearLayoutManager);

        SongListAdapter songListAdapter = new SongListAdapter(getContext(), (SongListAdapter.ItemClickListener) song -> {

        });

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        bottomSheetBehavior.setDraggable(false);
        songListAdapter.setData(songList);

        recyclerViewData.setAdapter(songListAdapter);

        return  bottomSheetDialog;
    }
}
