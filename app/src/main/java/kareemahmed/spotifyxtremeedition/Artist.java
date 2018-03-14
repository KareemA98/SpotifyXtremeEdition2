package kareemahmed.spotifyxtremeedition;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by krimb on 14/03/2018.
 */
public class Artist {
    public static ArrayList<Integer> genreNumbers = new ArrayList<Integer>();
        public Artist(JSONArray genres) {
            for(int i = 0 ; i < genres.length(); i++){
                try{
                    if (MainActivity.genresLookup.contains(genres.getString(i))){
                        genreNumbers.add(MainActivity.genresLookup.indexOf(genres.getString(i)));
                    }
                    else{
                        MainActivity.genresLookup.add(genres.getString(i));
                        MainActivity.genreHolder.add(new Genres(genres.getString(i)));
                    }
                }
                catch (JSONException e){

                }
            }
        }

        public void addTracks(Tracks tracks) {
            for (int i = 0; i < genreNumbers.size(); i++) {
                MainActivity.genreHolder.get(genreNumbers.get(i)).addTrack(tracks);
            }
        }
}
