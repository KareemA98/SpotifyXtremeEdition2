package kareemahmed.spotifyxtremeedition;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

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

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private static final String CLIENT_ID = "88414718aa494ca28bc1f99ddecc7e27";
    private static final String REDIRECT_URI = "my-spotify-app-login://callback";
    private static final int REQUEST_CODE = 1337;
    public static String userId = "ahmedfamily";//todo only will allow my account to work.
    public static String mAccessToken;
    public static HashSet<Tracks> tracksHashSet;
    private List<Playlists> playlistlist = new ArrayList<>();
    static final String PROVIDER_NAME = "kareemahmed.spotifyxtremeedition.ExampleProvider";

    //todo Limited to only 20 playlist because of how i get them from the internet
    //todo allow to refresh playlists
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        if (mAccessToken == null) {
            AuthenticationRequest.Builder builder = //todo Make it only request once when the app is turned on.
                    new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "playlist-read-private", "playlist-modify-public", "playlist-modify-private"});
            builder.setShowDialog(true);
            AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginInBrowser(this, request);
        }
        //
        // uri playlist = Uri.parse("content://" + PROVIDER_NAME + "/students");
        //getContentResolver().delete(playlist, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        String url = "https://api.spotify.com/v1/me/playlists";
        Uri playlist = Uri.parse("content://" + PROVIDER_NAME + "/students");
        Cursor c = getContentResolver().query(playlist, null, null, null, null);
        if (c.getCount() == 0) {
            getUserPlaylists(url);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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

    public void getUserPlaylists(String url) {
        playlistlist.clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray playlistArray = response.getJSONArray("items");
                            for (int i = 0; i < playlistArray.length(); i++) {
                                JSONObject obj = playlistArray.getJSONObject(i);
                                String name = obj.getString("name");
                                String trackNumber = obj.getJSONObject("tracks").getString("total");
                                String image = "No image";
                                if (obj.getJSONArray("images").length() > 0) {
                                    image = obj.getJSONArray("images").getJSONObject(0).getString("url");
                                }
                                String id = obj.getString("id"); //todo Error if the playlist is empty to do with there being no image
                                MainActivity.userId = obj.getJSONObject("owner").getString("id");//todo this isn't called all the time and needs to be
                                String userId = obj.getJSONObject("owner").getString("id");
                                ContentValues values = new ContentValues();
                                values.put(ExampleProvider.COLUMN_NAME, name);
                                values.put(ExampleProvider.COLUMN_ID, id);
                                values.put(ExampleProvider.COLUMN_NOOFSONGS, trackNumber);
                                values.put(ExampleProvider.COLUMN_IMAGE, image);
                                values.put(ExampleProvider.COLUMN_USERID, userId);
                                Uri uri = getContentResolver().insert(ExampleProvider.CONTENT_URI, values);
                                System.out.println(uri.toString());
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
}
