package kareemahmed.spotifyxtremeedition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by krimb on 01/03/2018.
 */

public class Playlists implements Parcelable {
    private String name,trackNumber,image,id,userId;
    private MoviesAdapter mAdapter;
    public ArrayList<Tracks> trackList = new ArrayList<Tracks>();
    public Playlists() {

    }


    public Playlists(String name, String trackNumber, String image , String id , String userId) {
        this.name = name;
        this.trackNumber = trackNumber;
        this.image = image;
        this.id = id;
        this.userId = userId;
    }

    protected Playlists(Parcel in) {
        userId = in.readString();
        name = in.readString();
        trackNumber = in.readString();
        image = in.readString();
        id = in.readString();
    }

    public static final Creator<Playlists> CREATOR = new Creator<Playlists>() {
        @Override
        public Playlists createFromParcel(Parcel in) {
            return new Playlists(in);
        }

        @Override
        public Playlists[] newArray(int size) {
            return new Playlists[size];
        }
    };

    public String getName() {
        return name;
    }

    public ArrayList<Tracks> getTrackList() {return trackList;}

    public void setTitle(String name) {
        this.name = name;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void getTracks(String url, final Context context) {
        System.out.println("in Get Tracks");
        int amountOfSongs = Integer.parseInt(trackNumber);
        int offset = 0;
        do {
                String newUrl = (url + "?offset=" + offset + "&limit=50");
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, newUrl, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("responce");
                                String artisturls = "";
                                ArrayList<String> names = new ArrayList<String>();
                                ArrayList<String> albums = new ArrayList<String>();
                                ArrayList<String> releases = new ArrayList<String>();
                                ArrayList<String> durations = new ArrayList<String>();
                                ArrayList<String> explicits = new ArrayList<String>();
                                ArrayList<String> popularity = new ArrayList<String>();
                                JSONArray tracksArray = response.getJSONArray("items");
                                for (int i = 0; i < tracksArray.length(); i++) {
                                    JSONObject obj = tracksArray.getJSONObject(i);
                                    names.add(obj.getJSONObject("track").getString("name"));
                                    albums.add(obj.getJSONObject("track").getJSONObject("album").getString("name"));
                                    releases.add(obj.getJSONObject("track").getJSONObject("album").getString("release_date"));
                                    durations.add(obj.getJSONObject("track").getString("duration_ms"));
                                    explicits.add(obj.getJSONObject("track").getString("explicit"));
                                    popularity.add(obj.getJSONObject("track").getString("popularity"));
                                    artisturls += obj.getJSONObject("track").getJSONObject("album").getJSONArray("artists").getJSONObject(0).getString("id") + ",";
                                }
                                getMoreTrackInfo(names, albums , releases , durations , explicits , popularity , artisturls,context);
                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + MainActivity.mAccessToken);
                    return headers;
                }
            };
            AppSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
            offset+=50;
            amountOfSongs -=50;
        } while (amountOfSongs > 1);
    }

    public void getMoreTrackInfo(final ArrayList name , final ArrayList albums , final ArrayList releases , final ArrayList durations , final ArrayList explicits , final ArrayList popularity ,
                                 String artisturls , final Context context){
        System.out.println("In Get more info");
        artisturls = artisturls.substring(0, artisturls.length() - 1);
        String url ="https://api.spotify.com/v1/artists?ids=" + artisturls;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray artists = response.getJSONArray("artists");
                                for(int i = 0;i <artists.length() ; i++){
                                JSONArray genresJSON = response.getJSONArray("artists").getJSONObject(i).getJSONArray("genres");
                                String artistName = response.getJSONArray("artists").getJSONObject(i).getString("name");
                                String artistid = response.getJSONArray("artists").getJSONObject(i).getString("id");
                                Tracks tracks = new Tracks(name.get(i).toString(), albums.get(i).toString(),artistName , releases.get(i).toString(),
                                        durations.get(i).toString(),explicits.get(i).toString(),popularity.get(i).toString());
                                trackList.add(tracks);
                                    for(int j = 0 ; j < genresJSON.length(); j++) {
                                        Iterator itr = MainActivity.genreHolder.iterator();
                                        Boolean compare = true;
                                        int counter = 0;
                                        while (itr.hasNext()) {
                                            Genres genre = (Genres) itr.next();
                                            if (genre.compareName(genresJSON.getString(j))) {
                                                MainActivity.genreHolder.get(counter).addTrack(tracks);
                                                compare = false;
                                            }
                                            counter++;
                                        }
                                        if (compare) {
                                            Genres genre = new Genres(genresJSON.getString(j));
                                            genre.addTrack(tracks);
                                            MainActivity.genreHolder.add(genre);
                                        }
                                    }
                                System.out.println("Tracks added ");
                                }

                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + MainActivity.mAccessToken);
                    return headers;
                }
            };
            AppSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(userId);
        parcel.writeString(name);
        parcel.writeString(trackNumber);
        parcel.writeString(image);
        parcel.writeString(id);
    }
}
