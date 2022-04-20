package ie.app.musicplayer.Model;

public class Song {
    private int songId;
    private int duration;
    private String songName;
    private String songAlbum;
    private int songImage;
    private String songSinger;
    private String songURL;

    public Song(int songId, int duration, String songName, String songAlbum, int songImage, String songSinger, String songURL) {
        this.songId = songId;
        this.duration = duration;
        this.songName = songName;
        this.songAlbum = songAlbum;
        this.songImage = songImage;
        this.songSinger = songSinger;
        this.songURL = songURL;
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

    public String getSongURL() {
        return songURL;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

}
