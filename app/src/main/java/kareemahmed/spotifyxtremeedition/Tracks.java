package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 06/03/2018.
 */

public class Tracks {
    private String name, artistName, albumName , explicit ,release;
    private int  duration,popularity;

    public Tracks(String name, String albumName , String artistName , String release, String duration , String explicit , String popularity) {
        this.name = name;
        this.albumName = albumName;
        this.artistName = artistName;
        this.release = release;
        this.duration = Integer.parseInt(duration);
        this.explicit = explicit;
        this.popularity = Integer.parseInt(popularity);
    }
    public String getName(){return name;}
    public int getPopularity(){return popularity;}
    public int getDuration(){return duration;}
    public boolean getExplicit(){return Boolean.parseBoolean(explicit);}
}
