package ie.app.musicplayer.Utility;

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
}
