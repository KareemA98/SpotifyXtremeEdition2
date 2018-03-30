package kareemahmed.spotifyxtremeedition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PickSubCategory extends Activity {
    public TextView genre;
    public Playlists movie;
    private ArrayList<Filter> popularities = new ArrayList<Filter>();
    private ArrayList<Filter> explcits = new ArrayList<Filter>();
    private ArrayList<Filter> lengths = new ArrayList<Filter>(); //ToDo
    private ArrayList<Filter> years = new ArrayList<>();
    private ArrayList<Filter> genres = new ArrayList<Filter>();
    public static HashSet<Tracks> finalSet = new HashSet<>();

    private Boolean filtered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_sub_category);
        movie = getIntent().getParcelableExtra("Parcel Data");
        movie.getTracks("https://api.spotify.com/v1/users/" + movie.getUserId() + "/playlists/" + movie.getId() + "/tracks", getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    public void setRecyclerView(View view) {
        ArrayList<Filter> passArray = new ArrayList<>();
        if (!filtered) {
            filtersMade();
        }
        switch (view.getId()) {
            case (R.id.Genre):
                passArray = genres;
                break;
            case (R.id.Explicits):
                passArray = explcits;
                break;
            case (R.id.Popularity):
                passArray = popularities;
                break;
            case (R.id.Release):
                passArray = years;
                break;
            case (R.id.Duration):
                passArray = lengths;
                break;
        }
        RecyclerView recyclerView;
        FilterAdapter fAdapter;
        recyclerView = (RecyclerView) findViewById(R.id.genreRecycle);
        fAdapter = new FilterAdapter(passArray, getApplicationContext());
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
        recyclerView.setAdapter(fAdapter);
    }

    public void update(View view){
        finalSet = combining();
        TextView answer;
        answer = findViewById(R.id.answer);
        answer.setText(Integer.toString(finalSet.size()));
    }

    public HashSet<Tracks> combining() {
        HashSet<Tracks> genreSet = new HashSet<Tracks>();
        Iterator itr = genres.iterator();
        while (itr.hasNext()) {
            Filter genre = (Filter) itr.next();
            if (genre.isSelected()) {
                genreSet.addAll(genre.returnSongs());
            }
        }
        HashSet<Tracks> explicitSet = new HashSet<Tracks>();
        itr = explcits.iterator();
        while (itr.hasNext()) {
            Filter explicit = (Filter) itr.next();
            if (explicit.isSelected()) {
                explicitSet.addAll(explicit.returnSongs());
            }
        }
        HashSet<Tracks> lengthSet = new HashSet<Tracks>();
        itr = lengths.iterator();
        while (itr.hasNext()) {
            Filter length = (Filter) itr.next();
            if (length.isSelected()) {
                lengthSet.addAll(length.returnSongs());
            }
        }
        HashSet<Tracks> popularitySet = new HashSet<Tracks>();
        itr = popularities.iterator();
        while (itr.hasNext()) {
            Filter popularity = (Filter) itr.next();
            if (popularity.isSelected()) {
                popularitySet.addAll(popularity.returnSongs());
            }
        }
        HashSet<Tracks> yearsSet = new HashSet<Tracks>();
        itr = years.iterator();
        while (itr.hasNext()) {
            Filter years = (Filter) itr.next();
            if (years.isSelected()) {
                yearsSet.addAll(years.returnSongs());
            }
        }
        finalSet = new HashSet<Tracks>();
        finalSet.addAll(movie.trackList);
        if (genreSet.size() > 0) {
            finalSet.retainAll(genreSet);
        }
        if (explicitSet.size() > 0) {
            finalSet.retainAll(explicitSet);
        }
        if (lengthSet.size() > 0) {
            finalSet.retainAll(lengthSet);
        }
        if (popularitySet.size() > 0) {
            finalSet.retainAll(popularitySet);
        }
        if (yearsSet.size() > 0) {
            finalSet.retainAll(yearsSet);
        }
        System.out.println("Kill me");
        return finalSet;
    }

    public void filtersMade() {
        ArrayList<Tracks> trackList = movie.trackList;
        Iterator itr = trackList.iterator();
        popularities.add(new Filter("deep"));
        popularities.add(new Filter("indie"));
        popularities.add(new Filter("popular"));
        popularities.add(new Filter("very popular"));
        explcits.add(new Filter("Non-Explicit"));
        explcits.add(new Filter("Explicit"));
        lengths.add(new Filter("5+ Minutes"));
        lengths.add(new Filter("4 Minutes"));
        lengths.add(new Filter("3 Minutes"));
        lengths.add(new Filter("2 Minutes"));
        lengths.add(new Filter("less then 2 Minutes"));
        while (itr.hasNext()) {
            Tracks track = (Tracks) itr.next();
            genreFilter(track);
            popularityFilter(track);
            explicitFiler(track);
            durationFilter(track);
            yearFilter(track);
        }
        Collections.sort(genres);
        //Collections.sort(years);
        filtered = true;
    }

    public void popularityFilter(Tracks tracks) {
        int popularity = tracks.getPopularity();
        if (popularity > 0 && popularity < 26) {
            popularities.get(0).addTracks(tracks);
        } else if (popularity > 25 && popularity < 51) {
            popularities.get(1).addTracks(tracks);
        } else if (popularity > 50 && popularity < 76) {
            popularities.get(2).addTracks(tracks);
        } else {
            popularities.get(3).addTracks(tracks);
        }

    }

    public void explicitFiler(Tracks tracks) {
        if (tracks.getExplicit()) {
            explcits.get(0).addTracks(tracks);
        } else {
            explcits.get(1).addTracks(tracks);
        }
    }

    public void durationFilter(Tracks tracks) {
        int duration = tracks.getDuration();
        if (duration >= 300000) {
            lengths.get(0).addTracks(tracks);
        } else if (duration >= 240000) {
            lengths.get(1).addTracks(tracks);
        } else if (duration >= 180000) {
            lengths.get(2).addTracks(tracks);
        } else if (duration >= 120000) {
            lengths.get(3).addTracks(tracks);
        } else {
            lengths.get(4).addTracks(tracks);
        }
    }

    public void yearFilter(Tracks tracks) {
        String date = tracks.getRelease();
        date = date.substring(0, 4);
        Iterator itr = years.iterator();
        Boolean compare = true;
        int counter = 0;
        while (itr.hasNext()) {
            Filter year = (Filter) itr.next();
            if (year.compareName(date)) {
                years.get(counter).addTracks(tracks);
                compare = false;
            }
            counter++;
        }
        if (compare) {
            Filter year = new Filter(date);
            year.addTracks(tracks);
            years.add(year);
        }
    }

    public void genreFilter(Tracks tracks) {
        ArrayList<String> trackGenres = tracks.getGenres();
        for (int i = 0; i < trackGenres.size(); i++) {
            Iterator itr = genres.iterator();
            Boolean compare = true;
            int counter = 0;
            while (itr.hasNext()) {
                Filter genre = (Filter) itr.next();
                if (genre.compareName(trackGenres.get(i))) {
                    genres.get(counter).addTracks(tracks);
                    compare = false;
                    break;
                }
                counter++;
            }
            if (compare) {
                Filter genre = new Filter(trackGenres.get(i));
                genre.addTracks(tracks);
                genres.add(genre);
            }
        }
    }

    public void submit(View view) {
        finalSet = combining();
        Intent intent = new Intent(getApplicationContext(), TrackOverview.class);
        startActivity(intent);
    }
}
