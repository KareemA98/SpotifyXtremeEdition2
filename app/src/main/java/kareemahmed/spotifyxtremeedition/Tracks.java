package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 06/03/2018.
 */

public class Tracks {
    private String name, artistName, albumName , explicit , duration , popularity,release;

    public Tracks(String name, String albumName , String artistName , String release, String duration , String explicit , String popularity) {
        this.name = name;
        this.albumName = albumName;
        this.artistName = artistName;
        this.release = release;
        this.duration = duration;
        this.explicit = explicit;
        this.popularity = popularity;
    }
    public String getName(){return name;}
}
