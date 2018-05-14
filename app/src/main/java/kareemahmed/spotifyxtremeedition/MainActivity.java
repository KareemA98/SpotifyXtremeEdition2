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

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private static final String CLIENT_ID = "88414718aa494ca28bc1f99ddecc7e27";
    private static final String REDIRECT_URI = "my-spotify-app-login://callback";
    private static final int REQUEST_CODE = 1337;
    public static String userId;
    public static String mAccessToken;
    public static HashSet<Tracks> tracksHashSet;
    private List<Playlists> playlistlist = new ArrayList<>();
    static final String PROVIDER_NAME = "kareemahmed.spotifyxtremeedition.ExampleProvider";
    public static NotificationManager mNotifyManager;
    private ShareActionProvider mShareActionProvider;

    //todo if you logout and don't login nothing is showed. Not sure if this is correct or if i should add something.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        authentication();
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Uri playlist = Uri.parse("content://" + PROVIDER_NAME + "/playlists");
        Cursor c = getContentResolver().query(playlist, null, null, null, null);
        if (c.getCount() == 0) {
            getUserPlaylists("https://api.spotify.com/v1/me/playlists");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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
                Intent myShareIntent = new Intent(Intent.ACTION_SEND);
                startActivity(Intent.createChooser(myShareIntent,"iten share"));
                return true;
            case R.id.action_refresh:
                getContentResolver().delete(playlist, null, null);
                getUserPlaylists("https://api.spotify.com/v1/me/playlists");
                return true;
            case R.id.action_logout:
                getContentResolver().delete(playlist, null, null);
                mAccessToken = null;
                authentication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "https://stackandroid.com");
        return shareIntent;
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
                    getUserId();
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
        if (mAccessToken == null) {
            AuthenticationRequest.Builder builder = //todo Make it only request once when the app is turned on.
                    new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "playlist-read-private", "playlist-modify-public", "playlist-modify-private"});
            builder.setShowDialog(true);
            AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginInBrowser(this, request);
        }
    }

}
