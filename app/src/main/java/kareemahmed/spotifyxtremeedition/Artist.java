package kareemahmed.spotifyxtremeedition;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by krimb on 14/03/2018.
 */
public class Artist {
    private ArrayList<Integer> genreNumbers = new ArrayList<Integer>();
    private String id;
        public Artist(String id , JSONArray genres,Tracks tracks) {
            this.id = id;
            for(int i = 0 ; i < genres.length(); i++){
                try {
                    Iterator itr = MainActivity.genreHolder.iterator();
                    Boolean compare = true;
                    int counter = 0;
                    while (itr.hasNext()) {
                        Genres genre = (Genres) itr.next();
                        if (genre.compareName(genres.get(i).toString())) {
                            genreNumbers.add(counter);
                            compare = false;
                        }
                        counter++;
                    }
                    if(compare){
                        Genres genre = new Genres(genres.get(i).toString());
                        genre.addTrack(tracks);
                        MainActivity.genreHolder.add(genre);
                        genreNumbers.add(genreNumbers.size());
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

        public boolean compareId(String artistid){
            if(id.equals(artistid)){
                return true;
            }
            else {
                return false;
            }

        }
}
