package ie.app.musicplayer.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.Utility.Constant;

public class ReceiverActionBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Intent intentService = new Intent(context, PlayControlService.class);
        intentService.putExtra("Song", (Song) bundle.get("Song"));
        intentService.putExtra("Play Status", updateStatus((Constant.Status) bundle.get("Play Status")));

        context.startService(intentService);
    }

    private Constant.Status updateStatus(Constant.Status playStatus) {
        Log.v("ReceiverAction", String.valueOf(playStatus));
        if (playStatus == Constant.Status.OFF) {
            playStatus = Constant.Status.ON;
        } else {
            playStatus = Constant.Status.OFF;
        }
        return playStatus;
    }
}
