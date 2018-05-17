package kareemahmed.spotifyxtremeedition;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
// This is my main and only activity since ill used a fragmented view
public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    // these are values required for the authentication
    private static final String CLIENT_ID = "88414718aa494ca28bc1f99ddecc7e27";
    private static final String REDIRECT_URI = "my-spotify-app-login://callback";
    private static final int REQUEST_CODE = 1337;
    // i decided to make all these values public since they are used all over the project and need to be easily accessible.
    public static String userId;
    public static String mAccessToken;
    public static HashSet<Tracks> tracksHashSet;
    static final String PROVIDER_NAME = "kareemahmed.spotifyxtremeedition.PlaylistProvider";
    public static NotificationManager mNotifyManager;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // i instansiate my view pager which contains all my fragments
        viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        // calls a method which does authentication for the app
        authentication();
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        //this is where a query my database to see if i have any data
        Uri playlist = Uri.parse("content://" + PROVIDER_NAME + "/playlists");
        Cursor c = getContentResolver().query(playlist, null, null, null, null);
        // this checks if there is any data in the playlist if there isnt it calls a method to sort that out
        if (c.getCount() == 0) {
            getUserPlaylists("https://api.spotify.com/v1/me/playlists");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this inflates my menu at the top
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        // this adds a share action provider to the menu
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        setShareIntent(createShareIntent());
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Uri playlist = Uri.parse("content://" + PROVIDER_NAME + "/playlists");
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                // if the share button is pressed it opens the share action provider
                Intent myShareIntent = new Intent(Intent.ACTION_SEND);
                startActivity(Intent.createChooser(myShareIntent,"iten share"));
                return true;
            case R.id.action_refresh:
                // if the refresh button is pressed we delete the contents of the database and they are then refilled
                getContentResolver().delete(playlist, null, null);
                getUserPlaylists("https://api.spotify.com/v1/me/playlists");
                return true;
            case R.id.action_logout:
                // if we logout we delete the contents of the database then do authentication again
                getContentResolver().delete(playlist, null, null);
                mAccessToken = null;
                authentication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Intent createShareIntent() {
        // this creates the share action provider
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "This spotify app is amazing you can get it on the play store here :");
        return shareIntent;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // this is boilerplate code needed for the spotify authentication
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = AuthenticationResponse.fromUri(uri);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    mAccessToken = response.getAccessToken();
                    getUserId();
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

    public void getUserPlaylists(String url) {
        // this is my get request for getting the users playlist
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // if the request was successful im returned a json object which i need to parse
                            // within the object is an array of all the playlists which i iterate through
                            JSONArray playlistArray = response.getJSONArray("items");
                            for (int i = 0; i < playlistArray.length(); i++) {
                                // i retrieve all of the info i need from the json object
                                JSONObject obj = playlistArray.getJSONObject(i);
                                String name = obj.getString("name");
                                String trackNumber = obj.getJSONObject("tracks").getString("total");
                                String image = "No image";
                                // if there are no songs in a playlist it has no image so this stops that error
                                if (obj.getJSONArray("images").length() > 0) {
                                    image = obj.getJSONArray("images").getJSONObject(0).getString("url");
                                }
                                String id = obj.getString("id");
                                String userId = obj.getJSONObject("owner").getString("id");
                                // i put all the data i collected in to the database
                                ContentValues values = new ContentValues();
                                values.put(PlaylistProvider.COLUMN_NAME, name);
                                values.put(PlaylistProvider.COLUMN_ID, id);
                                values.put(PlaylistProvider.COLUMN_NOOFSONGS, trackNumber);
                                values.put(PlaylistProvider.COLUMN_IMAGE, image);
                                values.put(PlaylistProvider.COLUMN_USERID, userId);
                                Uri uri = getContentResolver().insert(PlaylistProvider.CONTENT_URI, values);
                            }
                            // there may be more then 20 playlists so this redoes the method for the rest of the playlists
                            if((response.getString("next")) != "null"){
                                getUserPlaylists(response.getString("next"));
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
        AppSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void getUserId(){
        // this method is a get request to get the users userID
        String url = "https://api.spotify.com/v1/me";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            userId = response.getString("id");
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
        AppSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void authentication() {
        // this is boilerplate code for the spotify authentication
        if (mAccessToken == null) {
            AuthenticationRequest.Builder builder =
                    new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "playlist-read-private", "playlist-modify-public", "playlist-modify-private"});
            builder.setShowDialog(true);
            AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginInBrowser(this, request);
        }
    }

}
