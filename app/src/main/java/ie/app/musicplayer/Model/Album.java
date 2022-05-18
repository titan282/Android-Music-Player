package ie.app.musicplayer.Model;

import java.util.List;

public class Album {

    private String albumName;
    private List<Song> albumData;

    public Album(String albumName, List<Song> albumData) {
        this.albumName = albumName;
        this.albumData = albumData;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<Song> getAlbumData() {
        return albumData;
    }

    public void setAlbumData(List<Song> albumData) {
        this.albumData = albumData;
    }
}
