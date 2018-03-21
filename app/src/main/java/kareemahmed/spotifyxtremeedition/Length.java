package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 21/03/2018.
 */

public class Length {
    private ArrayList<Tracks> songs = new ArrayList<Tracks>();
    private String name;
    public Length(String name){
        this.name = name;
    }

    public void addTracks(Tracks tracks) {
        songs.add(tracks);
    }

    public String getSize() {
        return Integer.toString(songs.size());
    }
}
