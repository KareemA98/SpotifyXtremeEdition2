package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 23/03/2018.
 */

public class Filter implements Comparable<Filter> {
    private ArrayList<Tracks> songs = new ArrayList<Tracks>();
    private String name;
    private Boolean selected = false;

    public Filter(String name) {
        this.name = name;
    }

    public void addTracks(Tracks tracks) {
        songs.add(tracks);
    }

    public ArrayList<Tracks> returnSongs() {
        return songs;
    }

    public String getSize() {
        return Integer.toString(songs.size());
    }

    public String getName() {
        return name;
    }

    public int getSizeInteger(){
        return songs.size();
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setChecked(boolean checked) {
        selected = checked;
    }

    public int compareTo(Filter filter){
        int size = getSizeInteger();
        if(size == filter.getSizeInteger())
            return 0;
        else if(size>filter.getSizeInteger())
            return -1;
        else
            return 1;
    }
    public Boolean compareName(String newName) {
        return this.getName().equals(newName);
    }
}
