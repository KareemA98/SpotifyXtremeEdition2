package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;

/**
 * Created by krimb on 21/03/2018.
 */


public class Years implements Comparable<Years> {
    private ArrayList<Tracks> songs = new ArrayList<Tracks>();
    private String name;
    private Boolean selected = false;
    public Years(String name){
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

    public int getNameInteger() {return Integer.parseInt(name);}

    public Boolean compareYear(String newYear) {
        if (name.equals(newYear)){
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

    public int compareTo(Years years){
        int year = Integer.parseInt(name);
        if(year == years.getNameInteger())
            return 0;
        else if(year>years.getNameInteger())
            return -1;
        else
            return 1;
    }
}