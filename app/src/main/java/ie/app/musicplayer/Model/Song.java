package ie.app.musicplayer.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import ie.app.musicplayer.Application.MusicPlayerApp;

public class Song implements Parcelable {
    private int songId;
//    private int duration;
    private String songName;
    private String songAlbum;
    private int songImage;
    private String songSinger;
    private String songURL;
    private Bitmap songEmbeddedPicture;

    public Song(){}
    public Song(int songId, String songName, String songAlbum, int songImage, String songSinger, String songURL) {
        this.songId = songId;
//        this.duration = duration;
        this.songName = songName;
        this.songAlbum = songAlbum;
        this.songImage = songImage;
        this.songSinger = songSinger;
        this.songURL = songURL;
    }

    protected Song(Parcel in) {
        songId = in.readInt();
//        duration = in.readInt();
        songName = in.readString();
        songAlbum = in.readString();
        songImage = in.readInt();
        songSinger = in.readString();
        songURL = in.readString();
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getSongImage() {
        return songImage;
    }

    public void setSongImage(int songImage) {
        this.songImage = songImage;
    }

    public String getSongSinger() {
        return songSinger;
    }

    public void setSongSinger(String songSinger) {
        this.songSinger = songSinger;
    }

    public Bitmap getSongEmbeddedPicture() {
        return songEmbeddedPicture;
    }

    public void setSongEmbeddedPicture(Bitmap songEmbeddedPicture) {
        this.songEmbeddedPicture = songEmbeddedPicture;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getSongURL() {
        return songURL;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

//    public void setDuration(int duration) {
//        this.duration = duration;
//    }

//    public int getDuration() {
//        return duration;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(songId);
//        parcel.writeInt(duration);
        parcel.writeString(songName);
        parcel.writeString(songAlbum);
        parcel.writeInt(songImage);
        parcel.writeString(songSinger);
        parcel.writeString(songURL);
    }

    public void loadEmbeddedPicture() {
        if (songURL == null) {
            Log.d("loadEmbeddedPicture - Song class", toString());
        }

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(songURL);
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                songEmbeddedPicture = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mmr.release();
    }
}
