package ie.app.musicplayer.Activity;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ie.app.musicplayer.Application.MusicPlayerApp;

import ie.app.musicplayer.Database.DBManager;
import ie.app.musicplayer.Fragment.PlayControlBottomSheetFragment;
import ie.app.musicplayer.Fragment.SongFragment;
import ie.app.musicplayer.Model.Playlist;
import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;


public class PlayControlActivity extends AppCompatActivity implements PlayControlBottomSheetFragment.IOnItemSelectedListener {
    public enum Status {OFF, SINGLE, WHOLE, ON}

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
    private Thread changeSongThread, setInfoThread;
    private PlayControlBottomSheetFragment bottomSheetFragment;
    private Status shuffleStatus = Status.OFF;
    private Status loopStatus = Status.OFF;
    private Status favoriteStatus = Status.OFF;
  
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
        originalSongList = new ArrayList<Song>(songList);
        position = getPosition();
        setInfoToLayout(songList.get(position));
        initMediaPlayer(songList.get(position).getSongURL());
        setTimeTotal();
        updateTimeSong();

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

    private void back() {
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
                favoriteStatus = Status.ON;
                List<Playlist> playlists = Playlist.listAll(Playlist.class);
                playlists.get(0).getSongList().add(song);
                playlists.get(0).save();
                Log.v("song","FavoriteSize: "+Playlist.listAll(Playlist.class).get(0).getSongList().size());
                Toast.makeText(this, "Add song to Favorites successfully!",Toast.LENGTH_SHORT).show();
                 break;
            case ON:
                favoriteBtn.setImageResource(R.drawable.ic_favorite_border);
                favoriteStatus = Status.OFF;
                List<Playlist> playlists2 = Playlist.listAll(Playlist.class);
                int postionSong = getPostionInPlaylist(song, playlists2.get(0));
                playlists2.get(0).getSongList().remove(postionSong);
                playlists2.get(0).save();
                Log.v("song","FavoriteSize: "+Playlist.listAll(Playlist.class).get(0).getSongList().size());
                Toast.makeText(this, "Remove song from Favorites successfully!",Toast.LENGTH_SHORT).show();
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
//        dbManager.close();
        mediaPlayer.stop();
//        mediaPlayer.release();
    }


    private void playpause() {
        if (mediaPlayer.isPlaying()) {
            pauseMusic();
        } else {
            playMusic();
        }
    }

    private void next() {
        switch (loopStatus) {
            case WHOLE:
            case OFF:
                position = position < songList.size() - 1 ? ++position : 0;
                break;
        }

        changeSong();
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
                loopStatus = Status.WHOLE;
                loopBtn.setImageResource(R.drawable.ic_repeat_whole);
                break;
            case WHOLE:
                loopStatus = Status.SINGLE;
                loopBtn.setImageResource(R.drawable.ic_repeat_one);
                break;
            default:
                loopStatus = Status.OFF;
                loopBtn.setImageResource(R.drawable.ic_repeat);
                break;
        }
    }

    private void shuffle() {
        switch (shuffleStatus) {
            case OFF:
                shuffleStatus = Status.ON;
                shuffleBtn.setImageResource(R.drawable.ic_shuffle_on);
                Song currentSong = songList.get(position);
                Collections.shuffle(songList);
                position = songList.indexOf(currentSong);

                bottomSheetFragment.updateSongListAdapter(songList);
                // Shuffle songList Here
                break;
            default:
                shuffleStatus = Status.OFF;
                currentSong = songList.get(position);
                songList = new ArrayList<>(originalSongList);
                position = songList.indexOf(currentSong);
                shuffleBtn.setImageResource(R.drawable.ic_shuffle);

                bottomSheetFragment.updateSongListAdapter(songList);
                break;
        }
    }

    private void playMusic() {
        mediaPlayer.start();
        playPauseBtn.setImageResource(R.drawable.ic_pause);
    }

    private void pauseMusic() {
        mediaPlayer.pause();
        playPauseBtn.setImageResource(R.drawable.ic_play_arrow);
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
        position = (int) bundle.get("Position");
        return position;
    }

    private List<Song> getSongList() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return null;
        }
        return (List<Song>) bundle.get("Playlist");
    }

    private void setTimeTotal() {
        SimpleDateFormat time = new SimpleDateFormat("mm:ss");
        duration.setText(time.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void setInfoToLayout(Song song) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                songName.setText(song.getSongName());
                singerName.setText(song.getSongSinger());
                if(getPostionInPlaylist(song,Playlist.listAll(Playlist.class).get(0))!=-1){
                    favoriteBtn.setImageResource(R.drawable.ic_favorite);
                    favoriteStatus = Status.ON;
                }
                else {
                    favoriteBtn.setImageResource(R.drawable.ic_favorite_border);
                    favoriteStatus = Status.OFF;
                }
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(song.getSongURL());
                byte[] artBytes = mmr.getEmbeddedPicture();
                if (artBytes != null) {
                    InputStream is = new ByteArrayInputStream(artBytes);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    songPicture.setImageBitmap(bitmap);
                } else {
                    songPicture.setImageResource(song.getSongImage());
                }
                mmr.release();
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
            mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.start();
            });
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
                        Toast.makeText(PlayControlActivity.this,"Next",Toast.LENGTH_SHORT).show();
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