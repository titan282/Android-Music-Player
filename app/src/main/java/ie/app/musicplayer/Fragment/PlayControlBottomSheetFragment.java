package ie.app.musicplayer.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ie.app.musicplayer.Activity.PlayControlActivity;
import ie.app.musicplayer.Adapter.SongListAdapter;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class PlayControlBottomSheetFragment extends BottomSheetDialogFragment {

    private ImageButton shuffleBtn, loopBtn;
    private List<Song> songList;
    private IOnItemSelectedListener iOnItemSelectedListener;
    private PlayControlActivity.Status shuffleStatus = PlayControlActivity.Status.OFF;
    private PlayControlActivity.Status loopStatus = PlayControlActivity.Status.OFF;
    private SongListAdapter songListAdapter;
    private RecyclerView recyclerViewData;
    public interface IOnItemSelectedListener {
        void getSong(Song song);

        void getShuffleStatus();

        void getLoopStatus();
    }

    public PlayControlBottomSheetFragment(List<Song> songList, PlayControlActivity.Status shuffleStatus,
                                          PlayControlActivity.Status loopStatus) {
        this.songList = songList;
        this.shuffleStatus = shuffleStatus;
        this.loopStatus = loopStatus;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iOnItemSelectedListener = (IOnItemSelectedListener) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_fragment_play_control, null);
        bottomSheetDialog.setContentView(view);

        recyclerViewData = view.findViewById(R.id.songView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewData.setLayoutManager(linearLayoutManager);

        songListAdapter = new SongListAdapter(getContext(), song -> {
            iOnItemSelectedListener.getSong(song);
        });

        shuffleBtn = view.findViewById(R.id.shuffleBtn);
        shuffleBtn.setOnClickListener(view1 -> {
            shuffle();
            iOnItemSelectedListener.getShuffleStatus();
        });

        loopBtn = view.findViewById(R.id.loopBtn);
        loopBtn.setOnClickListener(view1 -> {
            loop();
            iOnItemSelectedListener.getLoopStatus();
        });

        setButtonImage();

        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        bottomSheetBehavior.setDraggable(false);
        songListAdapter.setData(songList);

        recyclerViewData.setAdapter(songListAdapter);

        return  bottomSheetDialog;
    }

    private void loop() {
        switch (loopStatus) {
            case OFF:
                loopStatus = PlayControlActivity.Status.WHOLE;
                loopBtn.setImageResource(R.drawable.ic_repeat_whole);
                break;
            case WHOLE:
                loopStatus = PlayControlActivity.Status.SINGLE;
                loopBtn.setImageResource(R.drawable.ic_repeat_one);
                break;
            default:
                loopStatus = PlayControlActivity.Status.OFF;
                loopBtn.setImageResource(R.drawable.ic_repeat);
                break;
        }
    }

    private void shuffle() {
        if (shuffleStatus == PlayControlActivity.Status.OFF) {
            shuffleStatus = PlayControlActivity.Status.ON;
            shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
        } else {
            shuffleStatus = PlayControlActivity.Status.OFF;
            shuffleBtn.setImageResource(R.drawable.ic_shuffle);
        }
    }

    public void updateSongListAdapter(List<Song> songList) {
        songListAdapter.setData(songList);
        Log.v("BottomSheet", "Notify dataset changed");
    }

    private void setButtonImage() {
        if (shuffleStatus == PlayControlActivity.Status.OFF) {
            shuffleBtn.setImageResource(R.drawable.ic_shuffle);
        } else {
            shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
        }

        switch (loopStatus) {
            case OFF:
                loopBtn.setImageResource(R.drawable.ic_repeat);
                break;
            case WHOLE:
                loopBtn.setImageResource(R.drawable.ic_repeat_whole);
                break;
            default:
                loopBtn.setImageResource(R.drawable.ic_repeat_one);
                break;
        }
    }
}
