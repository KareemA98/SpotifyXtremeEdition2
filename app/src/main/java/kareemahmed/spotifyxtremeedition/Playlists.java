package kareemahmed.spotifyxtremeedition;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by krimb on 01/03/2018.
 * This is my playlist class it contains all the methods and attributes for my playlists
 */

public class Playlists implements Parcelable {
    private String name,trackNumber,image,id,userId;
    public ArrayList<Tracks> trackList = new ArrayList<Tracks>();
    // for my contructor i take in a database cursor and then assign all of the data into the correct attributes
    public Playlists(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(PlaylistProvider.COLUMN_NAME));
        this.trackNumber = cursor.getString(cursor.getColumnIndex(PlaylistProvider.COLUMN_NOOFSONGS));
        this.image = cursor.getString(cursor.getColumnIndex(PlaylistProvider.COLUMN_IMAGE));
        this.id = cursor.getString(cursor.getColumnIndex(PlaylistProvider.COLUMN_ID));
        this.userId = cursor.getString(cursor.getColumnIndex(PlaylistProvider.COLUMN_USERID));
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
// this is the get request for all tbe tracks that are inclueded in a playlist
    public void getTracks(int offset ,int amountOfSongs, String url, final Context context) {
        // i need to manually change the offset as i can only get 50 songs at a tim
        String newUrl = (url + "?offset=" + offset + "&limit=50");
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                   (Request.Method.GET, newUrl, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // i collect all the info i need per song and put them into arraylists.
                                String artisturls = "";
                                ArrayList<String> names = new ArrayList<String>();
                                ArrayList<String> albums = new ArrayList<String>();
                                ArrayList<String> releases = new ArrayList<String>();
                                ArrayList<String> durations = new ArrayList<String>();
                                ArrayList<String> explicits = new ArrayList<String>();
                                ArrayList<String> popularity = new ArrayList<String>();
                                ArrayList<String> uri = new ArrayList<>();
                                JSONArray tracksArray = response.getJSONArray("items");
                                for (int i = 0; i < tracksArray.length(); i++) {
                                    JSONObject obj = tracksArray.getJSONObject(i);
                                    names.add(obj.getJSONObject("track").getString("name"));
                                    albums.add(obj.getJSONObject("track").getJSONObject("album").getString("name"));
                                    releases.add(obj.getJSONObject("track").getJSONObject("album").getString("release_date"));
                                    durations.add(obj.getJSONObject("track").getString("duration_ms"));
                                    explicits.add(obj.getJSONObject("track").getString("explicit"));
                                    popularity.add(obj.getJSONObject("track").getString("popularity"));
                                    uri.add(obj.getJSONObject("track").getString("uri"));
                                    // i create a long string of all the artists for the second get request that im going to do.
                                    artisturls += obj.getJSONObject("track").getJSONObject("album").getJSONArray("artists").getJSONObject(0).getString("id") + ",";
                                }
                                // calling the second get request to get the genre information.
                                getMoreTrackInfo(names, albums, releases, durations, explicits, popularity, uri, artisturls, context);
                                }
                            catch (JSONException e) {
                                throw new RuntimeException(e);

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
            // this allows my while loop to go through all the songs in a playlist.
            offset+=50;
            if (offset < amountOfSongs) {
                getTracks(offset,amountOfSongs,url,context);
            }
    }
    // this get request is done to collect genre information about each song -
    public void getMoreTrackInfo(final ArrayList name , final ArrayList albums , final ArrayList releases , final ArrayList durations , final ArrayList explicits , final ArrayList popularity ,
                                 final ArrayList uri ,String artisturls , final Context context){
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
                                ArrayList<String> list = new ArrayList<String>();
                                if(genresJSON != null){
                                    for (int j = 0 ; j < genresJSON.length();j++){
                                        list.add(genresJSON.get(j).toString());
                                    }
                                }
                                // this is where i create tracnks then i put them in this playlists tracklist to do what i want with.
                                Tracks tracks = new Tracks(name.get(i).toString(), albums.get(i).toString(),artistName , releases.get(i).toString(),
                                        durations.get(i).toString(),explicits.get(i).toString(),popularity.get(i).toString() , uri.get(i).toString(),list);
                                trackList.add(tracks);
                                }
                                LoadingScreen.increment();
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
