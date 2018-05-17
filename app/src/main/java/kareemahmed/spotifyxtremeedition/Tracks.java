package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 06/03/2018.
 * this is a class for singuler tracks in my app
 */

public class Tracks {
    private String name, artistName, albumName , explicit ,release,uri;
    private int  duration,popularity;
    private ArrayList<String> genres;
    // i pass all the info on the track when it is made.
    public Tracks(String name, String albumName , String artistName , String release, String duration , String explicit , String popularity , String uri ,ArrayList<String> genres) {
        this.name = name;
        this.albumName = albumName;
        this.artistName = artistName;
        this.release = release;
        this.duration = Integer.parseInt(duration);
        this.explicit = explicit;
        this.popularity = Integer.parseInt(popularity);
        this.genres = genres;
        this.uri = uri;
    }
    // bunch of getters for all the attributes.
    public String getName(){return name;}
    public String getUri(){return uri;}
    public String getArtistName(){return artistName;}
    public String getAlbumName(){return albumName;}
    public int getPopularity(){return popularity;}
    public int getDuration(){return duration;}
    public boolean getExplicit(){return Boolean.parseBoolean(explicit);}
    public String getRelease() {return release;}
    public ArrayList<String> getGenres() {return genres;}
}
