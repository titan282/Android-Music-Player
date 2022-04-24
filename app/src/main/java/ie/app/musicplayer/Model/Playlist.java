package ie.app.musicplayer.Model;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;


public class Playlist implements Parcelable {

    private int playlistId;
    private String playlistName;
    private int playlistImage;
    private List<Song> songList;

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void addToSongList(Song song){
        songList.add(song);
    }

    public Playlist(int playlistId, String playlistName, int playlistImage, List<Song> songList) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.playlistImage = playlistImage;
        this.songList = songList;
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

    public int getPlaylistImage() {
        return playlistImage;
    }

    public void setPlaylistImage(int playlistImage) {
        this.playlistImage = playlistImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(playlistId);
        parcel.writeInt(playlistImage);
        parcel.writeString(playlistName);
        parcel.writeTypedList(songList);
    }

    protected Playlist(Parcel in) {
        playlistId = in.readInt();
        playlistName = in.readString();
        playlistImage = in.readInt();
        in.readTypedList(songList, Song.CREATOR);
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
}
