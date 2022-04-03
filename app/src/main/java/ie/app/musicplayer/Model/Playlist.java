package ie.app.musicplayer.Model;

public class Playlist {

    private int playlistId;
    private String playlistName;
    private String playlistImage;

    public Playlist(int playlistId, String playlistName, String playlistImage) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.playlistImage = playlistImage;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistImage() {
        return playlistImage;
    }

    public void setPlaylistImage(String playlistImage) {
        this.playlistImage = playlistImage;
    }
}
