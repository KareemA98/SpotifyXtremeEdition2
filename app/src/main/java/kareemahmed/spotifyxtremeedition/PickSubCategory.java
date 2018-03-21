package kareemahmed.spotifyxtremeedition;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class PickSubCategory extends Activity {
public TextView genre;
public Playlists movie;
private ArrayList<Popularity> popularities = new ArrayList<Popularity>();
private ArrayList<Explicit> explcits = new ArrayList<Explicit>();
private ArrayList<Length> lengths = new ArrayList<Length>(); //ToDo
    private Boolean filtered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_sub_category);
        movie = getIntent().getParcelableExtra("Parcel Data");
        MainActivity.artistHolder.clear();
        MainActivity.genreHolder.clear();
        movie.getTracks("https://api.spotify.com/v1/users/"+ movie.getUserId() + "/playlists/"+ movie.getId() + "/tracks",getApplicationContext());
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }
    public void sendMessage(View view) {
        ArrayList<Genres> sortedGenres = (ArrayList<Genres>)MainActivity.genreHolder.clone();
        Collections.sort(sortedGenres);
        RecyclerView recyclerView;
        GenreAdapter gAdapter;
        recyclerView = (RecyclerView) findViewById(R.id.genreRecycle);
        gAdapter = new GenreAdapter(sortedGenres,getApplicationContext());
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
        recyclerView.setAdapter(gAdapter);
    }
    public void setExplcits(View view) {
        if (!filtered) {
            filtersMade();
        }
        RecyclerView recyclerView;
        ExplicitAdapter eAdapter;
        recyclerView = (RecyclerView) findViewById(R.id.genreRecycle);
        eAdapter = new ExplicitAdapter(explcits,getApplicationContext());
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
        recyclerView.setAdapter(eAdapter);
    }
    public void setPopularities(View view) {
        if (!filtered) {
            filtersMade();
        }
        RecyclerView recyclerView;
        PopularityAdapter pAdapter;
        recyclerView = (RecyclerView) findViewById(R.id.genreRecycle);
        pAdapter = new PopularityAdapter(popularities,getApplicationContext());
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
        recyclerView.setAdapter(pAdapter);
    }
    public void filtersMade() {
        ArrayList<Tracks> trackList = movie.trackList;
        Iterator itr = trackList.iterator();
        popularities.clear();
        popularities.add(new Popularity("deep"));
        popularities.add(new Popularity("indie"));
        popularities.add(new Popularity("popular"));
        popularities.add(new Popularity("very popular"));
        explcits.add(new Explicit("Non-Explicit"));
        explcits.add(new Explicit("Explicit"));
        lengths.add(new Length("5+ Minutes"));
        lengths.add(new Length("4 Minutes"));
        lengths.add(new Length("3 Minutes"));
        lengths.add(new Length("2 Minutes"));
        lengths.add(new Length("less then 2 Minutes"));
        while (itr.hasNext()) {
            Tracks track = (Tracks) itr.next();
            popularityFilter(track);
            explicitFiler(track);
            durationFilter(track);
           // releaseFilter();
        }
        filtered = true;
    }
    public void popularityFilter(Tracks tracks){
        int popularity = tracks.getPopularity();
        if(popularity > 0 && popularity < 26){
            popularities.get(0).addTracks(tracks);
        }
        else if(popularity > 25 && popularity < 51){
            popularities.get(1).addTracks(tracks);
        }
        else if (popularity > 50 && popularity < 76){
            popularities.get(2).addTracks(tracks);
        }
        else{
            popularities.get(3).addTracks(tracks);
        }

    }
    public void explicitFiler(Tracks tracks){
        if(tracks.getExplicit()){
            explcits.get(0).addTracks(tracks);
        }
        else{
            explcits.get(1).addTracks(tracks);
        }
    }
    public void durationFilter(Tracks tracks){
        int duration = tracks.getDuration();
        if(duration >= 30000){
            lengths.get(0).addTracks(tracks);
        }
        else if (duration >= 240000 ){
            lengths.get(1).addTracks(tracks);
        }
        else if (duration >= 180000 ){
            lengths.get(2).addTracks(tracks);
        }
        else if (duration >= 120000 ){
            lengths.get(3).addTracks(tracks);
        }
        else{
            lengths.get(4).addTracks(tracks);
        }
    }
}
