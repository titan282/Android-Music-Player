package ie.app.musicplayer.Utility;

import java.util.Comparator;

import ie.app.musicplayer.Model.Album;
import ie.app.musicplayer.Model.Singer;
import ie.app.musicplayer.Model.Song;

public class Constant {
    public enum Status {OFF, SINGLE, WHOLE, ON, NONE}
    public static int PAUSE_REQUEST_CODE = 1;
    public static int NOTIFICATION_ID = 128;
    public static String POSITION_KEY = "position";
    public static String PLAYLIST_KEY = "playlist";
    public static String COVER_KEY = "cover";
    public static String SONG_KEY = "song";
    public static String NAME_KEY = "name";
    public static String NEXT_KEY = "next";
    public static String PREV_KEY = "previous";
    public static String PLAY_KEY = "play status";
    public static String RANDOM_KEY = "random";
    public static String SEND_TO_PLAY_CONTROL_ACTIVITY = "toPlayControl";

    public static Sorting sorting = new Sorting();

    public static Comparator<Song> songComparator = new Comparator<Song>() {
        @Override
        public int compare(Song song, Song t1) {
            return Constant.sorting.generator(song.getSongName()).compareTo(
                    Constant.sorting.generator(t1.getSongName()));
        }
    };

    public static Comparator<Singer> singerComparator = new Comparator<Singer>() {
        @Override
        public int compare(Singer singer, Singer t1) {
            return Constant.sorting.generator(singer.getSingerName()).compareTo(
                    Constant.sorting.generator(t1.getSingerName()));
        }
    };

    public static Comparator<Album> albumComparator = new Comparator<Album>() {
        @Override
        public int compare(Album album, Album t1) {
            return Constant.sorting.generator(album.getAlbumName()).compareTo(
                    Constant.sorting.generator(t1.getAlbumName()));
        }
    };
}
