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
import java.util.concurrent.TimeUnit;

public class PickSubCategory extends Activity {
public TextView genre;
public Playlists movie;
    private RecyclerView recyclerView;
    private GenreAdapter gAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_sub_category);
        movie = getIntent().getParcelableExtra("Parcel Data");
        MainActivity.genresLookup.clear();
        MainActivity.genreHolder.clear();
        movie.getTracks("https://api.spotify.com/v1/users/"+ movie.getUserId() + "/playlists/"+ movie.getId() + "/tracks",getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.genreRecycle);
        gAdapter = new GenreAdapter(MainActivity.genreHolder,getApplicationContext());
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
    public void sendMessage(View view) {
        Genres genre = MainActivity.genreHolder.get(8);
        ArrayList<Tracks> songs = genre.returnSongs();
        System.out.println(songs.get(0).getName());
    }
}
