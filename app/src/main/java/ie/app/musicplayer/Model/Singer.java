package ie.app.musicplayer.Model;

import java.util.List;

public class Singer {
    private String singerName;
    private List<Song> singerSong;

    public Singer(String singerName, List<Song> singerSong) {
        this.singerName = singerName;
        this.singerSong = singerSong;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public List<Song> getSingerSong() {
        return singerSong;
    }

    public void setSingerSong(List<Song> singerSong) {
        this.singerSong = singerSong;
    }
}
