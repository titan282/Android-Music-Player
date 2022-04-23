package ie.app.musicplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class PlayControlActivity extends AppCompatActivity {
    private ImageButton playPauseBtn, previousBtn, nextBtn, loopBtn, shuffleBtn;
    private TextView songName,singerName;
    private TextView duration, runtime;
    private Toolbar collapse;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private List<Song> originalSongList;
    private int position = 0;

    private enum Status {OFF, SINGLE, WHOLE, ON}
    private Status shuffleStatus = Status.OFF;
    private Status loopStatus = Status.OFF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_control);
        init();
        songList = getSongList();
        originalSongList = songList;
        position = getPosition();
        setInfoToLayout(songList.get(position));
        initMediaPlayer(songList.get(position).getSongURL());
        setTimeTotal();
        updateTimeSong();

        playPauseBtn.setOnClickListener(view -> {
            if(mediaPlayer.isPlaying()){
                pauseMusic();
            }
            else{
                playMusic();
            }
        });

        previousBtn.setOnClickListener(view -> {
            switch (loopStatus) {
                case WHOLE:
                case OFF:
                    position = position > 0 ? --position : songList.size() - 1;
                    break;
            }

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(songList.get(position).getSongURL()));
            setTimeTotal();
            setInfoToLayout(songList.get(position));
            mediaPlayer.start();
        });

        nextBtn.setOnClickListener(view -> {
            switch (loopStatus) {
                case WHOLE:
                case OFF:
                    position = position < songList.size() - 1 ? ++position : 0;
                    break;
            }

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(songList.get(position).getSongURL()));
            setTimeTotal();
            setInfoToLayout(songList.get(position));
            mediaPlayer.start();
        });

        loopBtn.setOnClickListener(view -> {
            switch (loopStatus) {
                case OFF:
                    loopStatus = Status.WHOLE;
                    loopBtn.setImageResource(R.drawable.iconrepeatwhole);
                    break;
                case WHOLE:
                    loopStatus = Status.SINGLE;
                    loopBtn.setImageResource(R.drawable.iconrepeatsingle);
                    break;
                default:
                    loopStatus = Status.OFF;
                    loopBtn.setImageResource(R.drawable.iconrepeat);
                    break;
            }
        });

        shuffleBtn.setOnClickListener(view -> {
            switch (shuffleStatus) {
                case OFF:
                    shuffleStatus = Status.ON;
                    shuffleBtn.setImageResource(R.drawable.iconsuffleon);
                    Song currentSong = songList.get(position);
                    Collections.shuffle(songList);
                    position = songList.indexOf(currentSong);
                    // Shuffle songList Here
                    break;
                default:
                    shuffleStatus = Status.OFF;
                    currentSong = songList.get(position);
                    songList = originalSongList;
                    position = songList.indexOf(currentSong);
                    shuffleBtn.setImageResource(R.drawable.iconsuffle);
                    break;
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
//        mediaPlayer.release();
    }
    private void playMusic(){
        mediaPlayer.start();
        playPauseBtn.setImageResource(R.drawable.play);
    }
    private void pauseMusic(){
        mediaPlayer.pause();
        playPauseBtn.setImageResource(R.drawable.pause);
    }
    private void init(){
        playPauseBtn = findViewById(R.id.playPauseBtn);
        nextBtn = findViewById(R.id.nextBtn);
        loopBtn =findViewById(R.id.loopBtn);
        previousBtn = findViewById(R.id.previousBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        songName = findViewById(R.id.songName);
        singerName = findViewById(R.id.singerName);
//        collapse = findViewById(R.id.collapse);
        duration = findViewById(R.id.textViewtimetotal);
        runtime = findViewById(R.id.textViewruntime);
        seekBar =findViewById(R.id.seekBartime);
    }

    private int getPosition(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return 0;
        }
        position = (int) bundle.get("Position");
        return position;
    }

    private List<Song> getSongList() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return null;
        }
        return (List<Song>) bundle.get("Playlist");
    }

    private void setTimeTotal(){
        SimpleDateFormat time = new SimpleDateFormat("mm:ss");
        duration.setText(time.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }
    private void setInfoToLayout(Song song){
        songName.setText(song.getSongName());
        singerName.setText(song.getSongSinger());
    }
    private void initMediaPlayer(String URL){
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        runtime.setText(""+mediaPlayer.getCurrentPosition());
        try {
            mediaPlayer.setDataSource(URL);
            mediaPlayer.prepare();
            mediaPlayer.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateTimeSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                runtime.setText(timeFormat.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        },100);
    }
}