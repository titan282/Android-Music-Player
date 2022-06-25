package ie.app.musicplayer.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

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
        song = (Song) bundle.get(Constant.SONG_KEY);
        if (bundle.containsKey(Constant.PLAY_KEY)) {
            playStatus = (Constant.Status) bundle.get(Constant.PLAY_KEY);
        }
        sendNotificationMedia(song);
        return START_NOT_STICKY;
    }

    public void sendNotificationMedia(Song song) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this,"tag");

        int pauseImageId;
        if (playStatus == Constant.Status.OFF) {
            pauseImageId = R.drawable.ic_play_arrow;
        } else {
            pauseImageId = R.drawable.ic_pause;
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, MusicPlayerApp.CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSubText("MusicPlayer")
                .setContentTitle(song.getSongName())
                .setSubText(song.getSongSinger())
                .setSmallIcon(R.drawable.ic_music)
                .addAction(R.drawable.ic_skip_previous, "Previous", sendPrevCommand()) // #0
                .addAction(pauseImageId, "Pause", sendPlayStatus())  // #1
                .addAction(R.drawable.ic_skip_next, "Next", sendNextCommand())  // #2
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2 /* #1: pause button */)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setSound(null);

//        mediaSessionCompat.setMetadata
//                (new MediaMetadataCompat.Builder()
//                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,song.getSongEmbeddedPicture())
//                        .putString(MediaMetadata.METADATA_KEY_TITLE, song.getSongName())
//                        .putString(MediaMetadata.METADATA_KEY_ARTIST, song.getSongSinger())
//                        .build()
//                );

        if (!song.isHasPic()) {
            Log.e("PlayControlService", "Dark");
            notification.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.black_image));
        } else {
            song.loadEmbeddedPicture();
            notification.setLargeIcon(song.getSongEmbeddedPicture());
        }
        startForeground(Constant.NOTIFICATION_ID, notification.build());
    }

    private PendingIntent sendNextCommand() {
        Intent toReceiver = new Intent(this, ReceiverActionBroadcast.class);
        toReceiver.putExtra(Constant.NEXT_KEY, true);
        toReceiver.putExtra(Constant.SONG_KEY, song);

        return PendingIntent.getBroadcast(this.getApplicationContext(), 5,
                toReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent sendPrevCommand() {
        Intent toReceiver = new Intent(this, ReceiverActionBroadcast.class);
        toReceiver.putExtra(Constant.PREV_KEY, true);
        toReceiver.putExtra(Constant.SONG_KEY, song);

        return PendingIntent.getBroadcast(this.getApplicationContext(), 6,
                toReceiver, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent sendPlayStatus() {
        Intent toReceiver = new Intent(this, ReceiverActionBroadcast.class);
        toReceiver.putExtra(Constant.PLAY_KEY, playStatus);
        toReceiver.putExtra(Constant.SONG_KEY, song);

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
