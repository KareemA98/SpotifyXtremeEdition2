package kareemahmed.spotifyxtremeedition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TrackOverview extends Activity {
    private HashSet<Tracks> track;
    private String m_Text = "";
    private Boolean finished = false;
    ArrayList<Tracks> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_overview);
        track = PickSubCategory.finalSet;
        list.addAll(track);
        RecyclerView recyclerView;
        TrackAdapter tAdapter;
        recyclerView = (RecyclerView) findViewById(R.id.trackRecyclerView);
        tAdapter = new TrackAdapter(list, getApplicationContext());
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
        recyclerView.setAdapter(tAdapter);
    }

    public void submit(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name of playlist");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                createPlaylist();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void createPlaylist() {
        System.out.println("Job done");
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("name", m_Text);
        } catch (JSONException e) {
        }

        String newUrl = "https://api.spotify.com/v1/users/" + MainActivity.userId + "/playlists";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, newUrl, postparams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Job done");
                        try {
                            addTracks(response.getString("id"));
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
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + MainActivity.mAccessToken);
                return headers;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    public void addTracks(final String playlistId) {
        int offset = 0;
        finished = false;
        do {
            JSONObject postparams = new JSONObject();
            JSONArray uris = new JSONArray();
            int i = offset;
            while(i < (offset + 100 ) && i < (list.size() - 1)){
                uris.put(list.get(i).getUri());
                i++;
            }
            if (offset > (list.size() - 100)){
                finished = true;
            }
            try {
                postparams.put("uris", uris);
            } catch (JSONException e) {
            }
            String newUrl = " https://api.spotify.com/v1/users/" + MainActivity.userId + "/playlists/" + playlistId + "/tracks";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, newUrl, postparams, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Job done");
                            if (finished) {
                                String uri = "spotify:user:" + MainActivity.userId + ":playlist:" + playlistId;
                                Intent launcher = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(launcher);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            System.out.println(new String(error.networkResponse.data));
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + MainActivity.mAccessToken);
                    return headers;
                }
            };
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
            offset += 100;
        }
            while (offset < list.size());
    }
}
