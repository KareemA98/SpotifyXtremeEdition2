package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 06/03/2018.
 */

public class Tracks {
    String name, artistName, albumName , explicit , duration , popularity;
    ArrayList<String> genres;

    public Tracks(String name, String albumName , String artistName , String duration , String explicit , String popularity , ArrayList<String> genres) {
        this.name = name;
        this.albumName = albumName;
        this.artistName = artistName;
        this.duration = duration;
        this.explicit = explicit;
        this.popularity = popularity;
        this.genres = genres;
    }
    public String getName(){return name;}
}
