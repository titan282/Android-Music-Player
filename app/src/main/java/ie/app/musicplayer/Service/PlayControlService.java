package ie.app.musicplayer.Service;

import android.app.Notification;
import android.app.NotificationManager;
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

        Log.e("PlayControlService", "On Start Command");
        Bundle bundle = intent.getExtras();
        song = (Song) bundle.get("Song");
        if (bundle.containsKey("Play Status")) {
            playStatus = (Constant.Status) bundle.get("Play Status");
        }
        sendNotificationMedia(song);
        return START_NOT_STICKY;
    }

    public void sendNotificationMedia(Song song) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this,"tag");

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.songName, song.getSongName());

//        if (song.isHasPic()) {
//            remoteViews.setImageViewBitmap(R.id.imageBackground, song.getSongEmbeddedPicture());
//            Log.e("PlayControlService", "setImageBitmap");
//        } else {
//            remoteViews.setImageViewResource(R.id.imageBackground, R.color.black);
//        }
        remoteViews.setOnClickPendingIntent(R.id.playPauseBtn, sendPlayStatus());
        remoteViews.setOnClickPendingIntent(R.id.nextBtn, sendNextCommand());
        remoteViews.setOnClickPendingIntent(R.id.previousBtn, sendPrevCommand());

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
                .setSmallIcon(R.drawable.ic_music)
                .setCustomContentView(remoteViews);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        Log.v("PlayControlService", song.toString());

        startForeground(Constant.NOTIFICATION_ID, notification.build());
//        managerCompat.notify(Constant.NOTIFICATION_ID,notification.build());
    }

    private PendingIntent sendNextCommand() {
        Intent toReceiver = new Intent(this, ReceiverActionBroadcast.class);
        toReceiver.putExtra("Next", true);
        toReceiver.putExtra("Song", song);

        return PendingIntent.getBroadcast(this.getApplicationContext(), 5,
                toReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent sendPrevCommand() {
        Intent toReceiver = new Intent(this, ReceiverActionBroadcast.class);
        toReceiver.putExtra("Previous", true);
        toReceiver.putExtra("Song", song);

        return PendingIntent.getBroadcast(this.getApplicationContext(), 6,
                toReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent sendPlayStatus() {
        Intent toReceiver = new Intent(this, ReceiverActionBroadcast.class);
        toReceiver.putExtra("Play Status", playStatus);
        toReceiver.putExtra("Song", song);

        return PendingIntent.getBroadcast(this.getApplicationContext(), Constant.PAUSE_REQUEST_CODE,
                toReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constant.NOTIFICATION_ID);
    }
}
