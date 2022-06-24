package ie.app.musicplayer.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.Utility.Constant;

public class ReceiverActionBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Constant.Status status = updateStatus((Constant.Status) bundle.get(Constant.PLAY_KEY));
        Intent intentService = new Intent(context, PlayControlService.class);
        intentService.putExtra(Constant.SONG_KEY, (Song) bundle.get(Constant.SONG_KEY));
        intentService.putExtra(Constant.PLAY_KEY, status);

        Intent toPlayControl = new Intent(Constant.SEND_TO_PLAY_CONTROL_ACTIVITY);
        toPlayControl.putExtra(Constant.SONG_KEY, (Song) bundle.get(Constant.SONG_KEY));
        toPlayControl.putExtra(Constant.PLAY_KEY, status);

        if (bundle.containsKey(Constant.NEXT_KEY)) {
            toPlayControl.putExtra(Constant.NEXT_KEY,true);
            toPlayControl.putExtra(Constant.PLAY_KEY, Constant.Status.OFF);
        }

        if (bundle.containsKey(Constant.PREV_KEY)) {
            toPlayControl.putExtra(Constant.PREV_KEY,true);
            toPlayControl.putExtra(Constant.PLAY_KEY, Constant.Status.OFF);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(toPlayControl);
        context.startService(intentService);
    }

    private Constant.Status updateStatus(Constant.Status playStatus) {
        if (playStatus == Constant.Status.OFF) {
            playStatus = Constant.Status.ON;
        } else {
            playStatus = Constant.Status.OFF;
        }
        return playStatus;
    }
}
