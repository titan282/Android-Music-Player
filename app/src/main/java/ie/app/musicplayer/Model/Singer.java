package ie.app.musicplayer.Model;

public class Singer {
    private int singerId;
    private String singerName;
    private String singerImage;

    public Singer(int singerId, String singerName, String singerImage) {
        this.singerId = singerId;
        this.singerName = singerName;
        this.singerImage = singerImage;
    }

    public int getSingerId() {
        return singerId;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerImage() {
        return singerImage;
    }

    public void setSingerImage(String singerImage) {
        this.singerImage = singerImage;
    }
}
