package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.parsers.SAXParser;

/**
 * Created by krimb on 23/03/2018.
 * This class is a what i make all of my filters from
 */

public class Filter  {
    //This array holds all the songs which are related to a certain filter for example all the songs from 2010
    private ArrayList<Tracks> songs = new ArrayList<Tracks>();
    // Name of the filter
    private String name;
    // A boolean to say weather the user has chosen this filter
    private Boolean selected = false;
    // Constructor to make a filter i only take in the name of the filter
    public Filter(String name) {
        this.name = name;
    }
    // a method used to add a Track to this filter
    public void addTracks(Tracks tracks) {
        songs.add(tracks);
    }
    //Getter for the Track Arraylist contained within
    public ArrayList<Tracks> returnSongs() {
        return songs;
    }
    // returns the number of songs that are attributed to this filter this is used for display info about it
    public String getSize() {
        return Integer.toString(songs.size());
    }
    //Getter for the name
    public String getName() {
        return name;
    }
    // returns the amount of songs but in an integer form
    public int getSizeInteger() {
        return songs.size();
    }
    // returns the name but in integer form. This method only applies for filters that are named after years
    public int getNameInteger() {
        return Integer.parseInt(name);
    }
    // returns weather this filter has been chosen
    public Boolean isSelected() {
        return selected;
    }
    // chnages the boolean selected to the opposite to infer if it has been deselected or selected.
    public void setChecked(boolean checked) {
        selected = checked;
    }
    // this comparator compares the size of the filters this is used for genres as i wanted that list to be in order of how many songs each genre has.
    static Comparator<Filter> getAttribute1Comparator() {
        return new Comparator<Filter>() {
            public int compare(Filter f1, Filter f2) {
                if (f1.getSizeInteger() == f2.getSizeInteger())
                    return 0;
                else if (f1.getSizeInteger() > f2.getSizeInteger())
                    return -1;
                else
                    return 1;
            }
        };
    }
    // this comparator compares the values of the name if it was an integer, this one is used for putting the years in order starting from 2018 back.
    static Comparator<Filter> getAttribute2Comparator() {
        return new Comparator<Filter>() {
            // compare using attribute 2
            public int compare(Filter f1,Filter f2){
                if (f1.getNameInteger() == f2.getNameInteger())
                    return 0;
                else if (f1.getNameInteger() > f2.getNameInteger())
                    return -1;
                else
                    return 1;
            }
        };
    }
    // this method returns if the name is the is equal. this is used to see if a filter of a certain name has already been made.
    public Boolean compareName(String newName) {
        return this.getName().equals(newName);
    }
}
