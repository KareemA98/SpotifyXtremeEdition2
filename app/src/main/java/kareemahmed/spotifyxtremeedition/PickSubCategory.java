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
import java.util.concurrent.TimeUnit;

public class PickSubCategory extends Activity {
public TextView genre;
public Playlists movie;
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

    public void dateRecorded() {
        
    }
}
