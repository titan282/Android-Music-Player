package ie.app.musicplayer.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;
import ie.app.musicplayer.Utility.Constant;

public class PlayControlService extends Service {

    private Constant.Status playStatus = Constant.Status.ON;
    private Song song;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        song = (Song) bundle.get("Song");
        playStatus = (Constant.Status) bundle.get("Play Status");
        sendNotificationMedia(song);
        return START_NOT_STICKY;
    }

    public void sendNotificationMedia(Song song) {

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this,"tag");

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.songName, song.getSongName());
        remoteViews.setOnClickPendingIntent(R.id.playPauseBtn, getPendingIntent());
        remoteViews.setImageViewResource(R.id.nextBtn, R.drawable.ic_skip_next);
        remoteViews.setImageViewResource(R.id.previousBtn, R.drawable.ic_skip_previous);
        if(playStatus == Constant.Status.OFF) {
            remoteViews.setImageViewResource(R.id.playPauseBtn, R.drawable.ic_play_arrow);
        } else {
            remoteViews.setImageViewResource(R.id.playPauseBtn, R.drawable.ic_pause);
        }


                NotificationCompat.Builder notification = new NotificationCompat.Builder(this, MusicPlayerApp.CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSubText("MusicPlayer")
                .setContentTitle(song.getSongName())
                .setSubText(song.getSongSinger())
                .setSmallIcon(R.drawable.ic_music);

//                notification.addAction(R.drawable.ic_skip_previous, "Previous",
//                        PendingIntent.getBroadcast(this, Constant.PAUSE_REQUEST_CODE, pauseIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT)) // #0
//                .addAction(playButtonImage, "Pause", null)  // #1
//                .addAction(R.drawable.ic_skip_next, "Next", null)
//                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
//                        .setShowActionsInCompactView(1 /* #1: pause button */)
//                        .setMediaSession(mediaSessionCompat.getSessionToken()))
//                .setOngoing(true);

                notification.setCustomContentView(remoteViews);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        Log.v("PlayControlService", song.toString());

        managerCompat.notify(100,notification.build());
    }

    private PendingIntent getPendingIntent() {
        Intent toReceiver = new Intent(this, ReceiverActionBroadcast.class);
        toReceiver.putExtra("Play Status", playStatus);
        toReceiver.putExtra("Song", song);

        Intent toPlayControl = new Intent("To PlayControlActivity");
        toPlayControl.putExtra("Song", song);
        toPlayControl.putExtra("Play Status", playStatus);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(toPlayControl);
        return PendingIntent.getBroadcast(this.getApplicationContext(), Constant.PAUSE_REQUEST_CODE,
                toReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
