package kareemahmed.spotifyxtremeedition;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;
import java.util.Map;
public class PlaylistFragment extends Fragment {
    private List<Playlists> playlistlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    public static String userId;

    public static PlaylistFragment newInstance(){
        PlaylistFragment playlistFragment = new PlaylistFragment();
        return playlistFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.playlist_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
         recyclerView = getView().findViewById(R.id.playlistRecyclerView);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.playlistRecyclerView);
        mAdapter = new MoviesAdapter(playlistlist, getActivity());
        recyclerView.setHasFixedSize(true);
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        // adding custom divider line with padding 16dp
        // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        String url = "https://api.spotify.com/v1/me/playlists";
        getUserPlaylists(url);
    }
    public void getUserPlaylists(String url)  {
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
                                String image = "No image";
                                if(obj.getJSONArray("images").length() > 0){
                                    image = obj.getJSONArray("images").getJSONObject(0).getString("url");
                                }
                                String id = obj.getString("id"); //todo Error if the playlist is empty to do with there being no image
                                MainActivity.userId = obj.getJSONObject("owner").getString("id");
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
                headers.put("Authorization","Bearer " + MainActivity.mAccessToken);
                return headers;
            }
        }
                ;
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }
}
