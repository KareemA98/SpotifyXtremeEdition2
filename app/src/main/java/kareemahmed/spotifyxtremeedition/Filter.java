package kareemahmed.spotifyxtremeedition;

import java.util.ArrayList;
import java.util.Comparator;

import javax.xml.parsers.SAXParser;

/**
 * Created by krimb on 23/03/2018.
 */

public class Filter  {
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

    public int getSizeInteger() {
        return songs.size();
    }

    public int getNameInteger() {
        return Integer.parseInt(name);
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setChecked(boolean checked) {
        selected = checked;
    }

    static Comparator<Filter> getAttribute1Comparator() {
        return new Comparator<Filter>() {
            // compare using attribute 1
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
    public Boolean compareName(String newName) {
        return this.getName().equals(newName);
    }
}
