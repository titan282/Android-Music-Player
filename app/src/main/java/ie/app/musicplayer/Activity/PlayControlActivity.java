package ie.app.musicplayer.Activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ie.app.musicplayer.Utility.Constant;
import ie.app.musicplayer.Application.MusicPlayerApp;
import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Fragment.PlayControlBottomSheetFragment;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;
import ie.app.musicplayer.Service.PlayControlService;


public class PlayControlActivity extends AppCompatActivity implements PlayControlBottomSheetFragment.IOnItemSelectedListener {

    private ImageButton playPauseBtn, previousBtn, nextBtn, loopBtn, shuffleBtn, showBtn,favoriteBtn,backBtn;
    private ImageView songPicture;
    private TextView songName, singerName;
    private TextView duration, runtime;
    private Toolbar collapse;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private DBManager dbManager;
    private List<Song> songList;
    private List<Song> originalSongList;
    private int position = 0;
    public MusicPlayerApp app;
    private Thread changeSongThread;
    private PlayControlBottomSheetFragment bottomSheetFragment;
    private Constant.Status shuffleStatus = Constant.Status.OFF;
    private Constant.Status loopStatus = Constant.Status.OFF;
    private Constant.Status favoriteStatus = Constant.Status.OFF;
    private Constant.Status playStatus = Constant.Status.ON;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle.containsKey(Constant.NEXT_KEY) && (boolean) bundle.get(Constant.NEXT_KEY)) {
                next();
                return;
            }

            if (bundle.containsKey(Constant.PREV_KEY) && (boolean) bundle.get(Constant.PREV_KEY)) {
                previous();
                return;
            }

            if (bundle.containsKey(Constant.PLAY_KEY)) {
                playStatus = (Constant.Status) bundle.get(Constant.PLAY_KEY);
                playpause();
            }
        }
    };

    @Override
    public void getSong(Song song) {
        position = songList.indexOf(song);
        playPauseBtn.setImageResource(R.drawable.ic_pause);
        changeSong();
    }

    @Override
    public void getShuffleStatus() {
        shuffle();
    }

    @Override
    public void getLoopStatus() {
        loop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MusicPlayerApp)getApplication();
        setContentView(R.layout.activity_play_control);
        init();
        songList = getSongList();
        originalSongList = new ArrayList<>(songList);
        position = getPosition();
        setInfoToLayout(songList.get(position));
        initMediaPlayer(songList.get(position).getSongURL());
        setTimeTotal();
        updateTimeSong();
        checkShuffle();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Constant.SEND_TO_PLAY_CONTROL_ACTIVITY));

        //Send notification
        updateNotificationService();

        playPauseBtn.setOnClickListener(view -> {
            playpause();
        });
        previousBtn.setOnClickListener(view -> {
            previous();
        });
        nextBtn.setOnClickListener(view -> {
            next();
        });
        loopBtn.setOnClickListener(view -> {
            loop();
        });
        shuffleBtn.setOnClickListener(view -> {
            shuffle();
        });
        favoriteBtn.setOnClickListener(view -> {
            addToFavorite(songList.get(position));
        });
        showBtn.setOnClickListener(view -> {
            showPlaylist();
        });
        backBtn.setOnClickListener(view -> {
            back();
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private void updateNotificationService() {
        Intent notificationIntent = new Intent(this, PlayControlService.class);
        notificationIntent.putExtra(Constant.SONG_KEY, songList.get(position));
        notificationIntent.putExtra(Constant.PLAY_KEY, playStatus);
        startService(notificationIntent);
    }

    private void checkShuffle() {
        Bundle bundle = getIntent().getExtras();
        Boolean isShuffle = bundle.getBoolean(Constant.RANDOM_KEY);
        if(isShuffle){
            shuffle();
        }
    }

    private void back() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
        onBackPressed();

    }


    private void showPlaylist() {
        bottomSheetFragment = new PlayControlBottomSheetFragment(songList, shuffleStatus, loopStatus);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }
    private void addToFavorite(Song song){
        switch(favoriteStatus){
            case OFF:
                favoriteBtn.setImageResource(R.drawable.ic_favorite);
                favoriteStatus = Constant.Status.ON;
                List<Playlist> playlists = Playlist.listAll(Playlist.class);
                playlists.get(0).getSongList().add(song);
                playlists.get(0).save();
                Log.v("song","FavoriteSize: "+Playlist.listAll(Playlist.class).get(0).getSongList().size());
                Toast.makeText(this, "Đã thêm bài hát vào Favorite!",Toast.LENGTH_SHORT).show();
                 break;
            case ON:
                favoriteBtn.setImageResource(R.drawable.ic_favorite_border);
                favoriteStatus = Constant.Status.OFF;
                List<Playlist> playlists2 = Playlist.listAll(Playlist.class);
                int positionSong = getPostionInPlaylist(song, playlists2.get(0));
                playlists2.get(0).getSongList().remove(positionSong);
                playlists2.get(0).save();
                Log.v("song","FavoriteSize: "+Playlist.listAll(Playlist.class).get(0).getSongList().size());
                Toast.makeText(this, "Đã xóa bài hát khỏi Favorite!",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int getPostionInPlaylist(Song song, Playlist playlist) {
        List<String> songUrl = new ArrayList<String>();
        for(Song songItem:playlist.getSongList()){
            songUrl.add(songItem.getSongURL());
        }
        return songUrl.indexOf(song.getSongURL());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, PlayControlService.class);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        stopService(intent);
        mediaPlayer.stop();
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
//        mediaPlayer.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }

    private void playpause() {
        if (mediaPlayer.isPlaying()) {
            pauseMusic();
            playStatus = Constant.Status.OFF;
        } else {
            playMusic();
            playStatus = Constant.Status.ON;
        }
        updateNotificationService();
    }

    private void next() {
        switch (loopStatus) {
            case WHOLE:
            case OFF:
                position = position < songList.size() - 1 ? ++position : 0;
                break;
        }

        changeSong();
        playPauseBtn.setImageResource(R.drawable.ic_pause);
    }

    private void previous() {
        switch (loopStatus) {
            case WHOLE:
            case OFF:
                position = position > 0 ? --position : songList.size() - 1;
                break;
        }

        changeSong();
    }

    private void loop() {
        switch (loopStatus) {
            case OFF:
                loopStatus = Constant.Status.WHOLE;
                loopBtn.setImageResource(R.drawable.ic_repeat_whole);
                break;
            case WHOLE:
                loopStatus = Constant.Status.SINGLE;
                loopBtn.setImageResource(R.drawable.ic_repeat_one);
                break;
            default:
                loopStatus = Constant.Status.OFF;
                loopBtn.setImageResource(R.drawable.ic_repeat);
                break;
        }
        updateNotificationService();
    }

    private void shuffle() {
        if (shuffleStatus == Constant.Status.OFF) {
            shuffleStatus = Constant.Status.ON;
            shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
            Song currentSong = songList.get(position);
            Collections.shuffle(songList);
            position = songList.indexOf(currentSong);

            // Shuffle songList Here
        } else {
            Song currentSong;
            shuffleStatus = Constant.Status.OFF;
            currentSong = songList.get(position);
            songList = new ArrayList<>(originalSongList);
            position = songList.indexOf(currentSong);
            shuffleBtn.setImageResource(R.drawable.ic_shuffle_off);

        }
        if (bottomSheetFragment != null) {
            bottomSheetFragment.updateSongListAdapter(songList);
        }
        updateNotificationService();
    }

    private void playMusic() {
        mediaPlayer.start();
        playPauseBtn.setImageResource(R.drawable.ic_pause);
//        songName.setSelected(true);
    }

    private void pauseMusic() {
        mediaPlayer.pause();
        playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
//        songName.setSelected(false);
    }

    private void changeSong() {
        changeSongThread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(songList.get(position).getSongURL()));
                setTimeTotal();
                setInfoToLayout(songList.get(position));
                mediaPlayer.start();
                playStatus = Constant.Status.ON;
                updateNotificationService();
                playPauseBtn.setImageResource(R.drawable.ic_pause);
            }
        };
        changeSongThread.run();
    }

    private void init() {
        playPauseBtn = findViewById(R.id.playPauseBtn);
        nextBtn = findViewById(R.id.nextBtn);
        loopBtn = findViewById(R.id.loopBtn);
        previousBtn = findViewById(R.id.previousBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        showBtn = findViewById(R.id.showPlaylist);
        favoriteBtn = findViewById(R.id.favoriteBtn);
        songName = findViewById(R.id.songName);
        songName.setSelected(true);
        songName.setMovementMethod(new ScrollingMovementMethod());
        songName.setHorizontallyScrolling(true);
        singerName = findViewById(R.id.singerName);
        duration = findViewById(R.id.textViewtimetotal);
        runtime = findViewById(R.id.textViewruntime);
        seekBar = findViewById(R.id.seekBartime);
        songPicture = findViewById(R.id.thumnail);
        backBtn = findViewById(R.id.backBtn);
    }

    private int getPosition() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return 0;
        }
        position = (int) bundle.get(Constant.POSITION_KEY);
        return position;
    }

    private List<Song> getSongList() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return null;
        }
        return (List<Song>) bundle.get(Constant.PLAYLIST_KEY);
    }

    private void setTimeTotal() {
        SimpleDateFormat time = new SimpleDateFormat("mm:ss");
        duration.setText(time.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void setInfoToLayout(Song song) {
        runOnUiThread(() -> {
            songName.setText(song.getSongName());
            songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            Log.d("Debug","songName= "+song.getSongName());
            singerName.setText(song.getSongSinger());
            if (song.isHasPic()) {
                song.checkPicStatusAndLoad();
                songPicture.setImageBitmap(song.getSongEmbeddedPicture());
            } else {
                songPicture.setImageResource(song.getSongImage());
                    }
                if(getPostionInPlaylist(song,Playlist.listAll(Playlist.class).get(0))!=-1){
                    favoriteBtn.setImageResource(R.drawable.ic_favorite);
                    favoriteStatus = Constant.Status.ON;
                }
                else {
                    favoriteBtn.setImageResource(R.drawable.ic_favorite_border);
                    favoriteStatus = Constant.Status.OFF;
                }
        });
    }

    private void initMediaPlayer(String URL) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

        }

        runtime.setText("" + mediaPlayer.getCurrentPosition());
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            mediaPlayer.setDataSource(URL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
            mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.start();
            });
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes
                            .Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build());
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateTimeSong() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        next();
                    }
                });
                if (mediaPlayer != null) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                    runtime.setText(timeFormat.format(mediaPlayer.getCurrentPosition()));
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
                handler.postDelayed(this, 100
                );
            }
        }, 100);
    }
}