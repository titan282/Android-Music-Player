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
        Constant.Status status = updateStatus((Constant.Status) bundle.get("Play Status"));
        Intent intentService = new Intent(context, PlayControlService.class);
        intentService.putExtra("Song", (Song) bundle.get("Song"));
        intentService.putExtra("Play Status", status);

        Intent toPlayControl = new Intent("To PlayControlActivity");
        toPlayControl.putExtra("Song", (Song) bundle.get("Song"));
        toPlayControl.putExtra("Play Status", status);

        if (bundle.containsKey("Next")) {
            toPlayControl.putExtra("Next",true);
            toPlayControl.putExtra("Play Status", Constant.Status.OFF);
        }

        if (bundle.containsKey("Previous")) {
            toPlayControl.putExtra("Previous",true);
            toPlayControl.putExtra("Play Status", Constant.Status.OFF);
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
