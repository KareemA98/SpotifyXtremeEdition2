package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 13/03/2018.
 */

public class Genres implements Comparable<Genres> {
    private ArrayList<Tracks> songs = new ArrayList<Tracks>();
    private String name;
    private Boolean selected = false;
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

    public String getSize() {
        return Integer.toString(songs.size());
    }

    public int getSizeInteger() {
        return songs.size();
    }

    public Boolean compareName(String newName) {
        if (name.equals(newName)){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean isSelected() {
        return selected;
    }
    public void setChecked(boolean checked) {
        selected = checked;
    }

    public int compareTo(Genres genres){
        int size = songs.size();
        if(size == genres.getSizeInteger())
            return 0;
        else if(size>genres.getSizeInteger())
            return -1;
        else
            return 1;
    }
}
