package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 13/03/2018.
 */

public class Genres {
    private ArrayList<Tracks> songs = new ArrayList<Tracks>();
    private String name;
    public Genres(String name){
        this.name = name;
    }

    public void addTrack(Tracks adder) {
        songs.add(adder);
    }

    public ArrayList<Tracks> returnSongs(){
        return songs;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return songs.size();
    }
}
