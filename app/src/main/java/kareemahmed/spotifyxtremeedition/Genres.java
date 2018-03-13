package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 13/03/2018.
 */

public class Genres {
    private ArrayList<Tracks> songs = new ArrayList<Tracks>();

    public Genres(){

    }

    public void addTrack(Tracks adder) {
        songs.add(adder);
    }
}
