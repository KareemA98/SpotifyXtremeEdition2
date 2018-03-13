package kareemahmed.spotifyxtremeedition;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
public class MainActivity extends Activity {
    private List<Playlists> playlistlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private static final String CLIENT_ID = "88414718aa494ca28bc1f99ddecc7e27";
    private static final String REDIRECT_URI = "my-spotify-app-login://callback";
    private static final int REQUEST_CODE = 1337;
    private Long AuthTimer;
    public static String mAccessToken;
    public static ArrayList<String> genresLookup = new ArrayList<String>();
    public static ArrayList<Genres> genreHolder = new ArrayList<Genres>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private","playlist-read-private","streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginInBrowser(this ,request);
        //getAuthentication();
        mAdapter = new MoviesAdapter(playlistlist,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        genreArray();
}
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = AuthenticationResponse.fromUri(uri);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    mAccessToken = response.getAccessToken();
                    System.out.println(response.getAccessToken());
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        String url = "https://api.spotify.com/v1/me/playlists";
        getUserPlaylists(url);
    }
    public void getUserPlaylists(String url) {
        playlistlist.clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray playlistArray = response.getJSONArray("items");
                            for(int i = 0; i < playlistArray.length();i++){
                                JSONObject obj = playlistArray.getJSONObject(i);
                                String name = obj.getString("name");
                                String trackNumber = obj.getJSONObject("tracks").getString("total");
                                String image = obj.getJSONArray("images").getJSONObject(0).getString("url");
                                String id = obj.getString("id");
                                String userId = obj.getJSONObject("owner").getString("id");
                                Playlists playlist = new Playlists(name,trackNumber,image,id,userId);
                                playlistlist.add(playlist);
                            }
                            mAdapter.notifyDataSetChanged();
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
                headers.put("Authorization","Bearer " + mAccessToken);
                return headers;
            }
        }
                ;
        AppSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
    public void getAuthentication() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private","playlist-read-private","streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginInBrowser(this ,request);
    }

    public void genreArray() {
        String url = "https://api.spotify.com/v1/recommendations/available-genre-seeds";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray genreArray = response.getJSONArray("genres");
                            for (int i = 0; i < genreArray.length(); i++) {
                                genresLookup.add(genreArray.getString(i));
                                genreHolder.add(new Genres());
                            }
                            mAdapter.notifyDataSetChanged();
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
                headers.put("Authorization", "Bearer " + mAccessToken);
                return headers;
            }
        };
    }

}
