package ie.app.musicplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import ie.app.musicplayer.Model.Song;
import ie.app.musicplayer.R;

public class PlayControlActivity extends AppCompatActivity {
    private ImageButton playPauseBtn, previousBtn, nextBtn, loopBtn, shuffleBtn;
    private TextView songName,singerName;
    private TextView duration, runtime;
    private Toolbar collapse;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_control);
        init();
        Song song = getSong();
        setInfoToLayout(song);
        initMediaPlayer(song.getSongURL());
        setTimeTotal();
        updateTimeSong();
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    pauseMusic();
                }
                else{
                    playMusic();
                }
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
    private Song getSong(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return null;
        }
        Song song = (Song) bundle.get("object_song");
        return song;
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
        mediaPlayer = new MediaPlayer();
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