package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 16/03/2018.
 */

public class Explicit {
    private ArrayList<Tracks> songs = new ArrayList<Tracks>();
    private String name;
    public Explicit(String name){
        this.name = name;
    }

    public void addTracks(Tracks tracks) {
        songs.add(tracks);
    }

    public String getSize() {
        return Integer.toString(songs.size());
    }

    public String getName() {return name;}
}
