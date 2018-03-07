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

    public void getTracks(String url, final Context context , final Intent intent) {
        System.out.println("in Get Tracks");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("responce");
                            JSONArray tracksArray = response.getJSONArray("items");
                            for(int i = 0; i < tracksArray.length();i++){
                                JSONObject obj = tracksArray.getJSONObject(i);
                                String name = obj.getJSONObject("track").getString("name");
                                getMoreTrackInfo(name,obj.getJSONObject("track").getString("href"),context);
                            }
                        }
                        catch (JSONException e){

                        }
                        context.startActivity(intent);
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
                headers.put("Authorization","Bearer " + MainActivity.mAccessToken);
                return headers;
            }
        }
                ;
        AppSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void getMoreTrackInfo(final String name , String url, final Context context){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String albumName = response.getJSONObject("album").getString("name");
                            String artistName = response.getJSONArray("artists").getJSONObject(0).getString("name");
                            String artistUrl = response.getJSONArray("artists").getJSONObject(0).getString("href");
                            String duration = response.getString("duration_ms");
                            String explicit = response.getString("explicit");
                            String popularity = response.getString("popularity");
                            getEvenMoreTrackInfo(albumName,artistName,artistUrl,name,duration,explicit,popularity,context);
                        }
                        catch (JSONException e){

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
                headers.put("Authorization","Bearer " + MainActivity.mAccessToken);
                return headers;
            }
        }
                ;
        AppSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void getEvenMoreTrackInfo(final String albumName , final String artistName , String url , final String name , final String duration , final String explicit , final String popularity , Context context){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<String> genres = new ArrayList<String>();
                             JSONArray genresJSON = response.getJSONArray("genres");
                             for (int i = 0; i < genresJSON.length();i++){
                                 genres.add(genresJSON.getString(i));
                             }
                            trackList.add(new Tracks(name, albumName,artistName,duration,explicit,popularity,genres));
                            System.out.println(trackList.get(0).getName());

                        }
                        catch (JSONException e){

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
                headers.put("Authorization","Bearer " + MainActivity.mAccessToken);
                return headers;
            }
        }
                ;
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
