package kareemahmed.spotifyxtremeedition;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.LoaderTestCase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
// this fragment allows you to filter your songs
public class SubCategoryFragment extends Fragment implements View.OnClickListener {
    public TextView genre;
    public Playlists movie;
    // these arraylists contains all of the different type of filters i can do. some have more then others
    private ArrayList<Filter> popularities = new ArrayList<Filter>();
    private ArrayList<Filter> explcits = new ArrayList<Filter>();
    private ArrayList<Filter> lengths = new ArrayList<Filter>(); //ToDo
    private ArrayList<Filter> years = new ArrayList<>();
    private ArrayList<Filter> genres = new ArrayList<Filter>();
    public static HashSet<Tracks> finalSet = new HashSet<>();
    private Boolean filtered = false;
    private RecyclerView recyclerView;

    public static SubCategoryFragment newInstance(){
        SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
        return subCategoryFragment;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        movie = getArguments().getParcelable("Parcel Data");
        // here i get the object that i passed through.
        // i have to round up so i do enough get requests.
        double trackNumber = Integer.parseInt(movie.getTrackNumber());
        // i call the loading screen as a bunch of get requests are gonna happen and then pass it the number of get requests are going to happen so it changes in length depending.
        new LoadingScreen(getActivity(), Math.ceil(trackNumber / 50));
        String url = "https://api.spotify.com/v1/users/" + movie.getUserId() + "/playlists/" + movie.getId() + "/tracks";
        movie.getTracks(0,Integer.parseInt(movie.getTrackNumber()),url, getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_pick_sub_category, container, false);
        Button genre = v.findViewById(R.id.Genre);
        Button explicit = v.findViewById(R.id.Explicits);
        Button popularity = v.findViewById(R.id.Popularity);
        Button release = v.findViewById(R.id.Release);
        Button duration = v.findViewById(R.id.Duration);
        Button update = v.findViewById(R.id.update);
        Button submit = v.findViewById(R.id.Submit);
        update.setOnClickListener(this);
        genre.setOnClickListener(this);
        explicit.setOnClickListener(this);
        popularity.setOnClickListener(this);
        release.setOnClickListener(this);
        duration.setOnClickListener(this);
        submit.setOnClickListener(this);
        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = getView().findViewById(R.id.genreRecycle);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }
    @Override
    public void onClick(View v) {
        // a switch for all my buttons
        switch (v.getId()) {
            case (R.id.Genre):
                setRecyclerView(genres);
                break;
            case (R.id.Explicits):
                setRecyclerView(explcits);
                break;
            case (R.id.Popularity):
                setRecyclerView(popularities);
                break;
            case (R.id.Release):
                setRecyclerView(years);
                break;
            case (R.id.Duration):
                setRecyclerView(lengths);
                break;
            case (R.id.Submit):
                submit();
                break;
            case (R.id.update):
                update();
                break;
        }
        // no matter what i press i call the combing method then update the counter at the bottom.
        finalSet = combining();
        TextView answer;
        answer = getView().findViewById(R.id.answer);
        answer.setText(Integer.toString(finalSet.size()));
        }

    public void setRecyclerView(ArrayList<Filter> passArray) {
        // this stops me from constantly calling the say method over and over again.
        if (!filtered) {
            filtersMade();
        }
        // this makes a recycler view.
        FilterAdapter fAdapter;
        fAdapter = new FilterAdapter(passArray);
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
        recyclerView.setAdapter(fAdapter);
    }

    public void update(){
        // this updates the amount of songs in the current filter marker.
        finalSet = combining();
        TextView answer;
        answer = getView().findViewById(R.id.answer);
        answer.setText(Integer.toString(finalSet.size()));
    }

    public HashSet<Tracks> combining() {
        // this method takes all of the filters ive made and combines all of the ones that have been selected
        // it goes through each arraylist and combines all of the selected filters
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
        // now ive found all the songs in each category i need to intersect them all which is what im doing here.
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
        return finalSet;
    }

    public void filtersMade() {
        // here i make some of the filters not all as some are made through going through the songs.
        ArrayList<Tracks> trackList = movie.trackList;
        Iterator itr = trackList.iterator();
        // i create all the popularites , explicts and lengths filters.
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
        // i go through each track in the playlist and put into each of the respective filters.
        while (itr.hasNext()) {
            Tracks track = (Tracks) itr.next();
            genreFilter(track);
            popularityFilter(track);
            explicitFiler(track);
            durationFilter(track);
            yearFilter(track);
        }
        // i sort the genre filters by the amount of songs in each
        Collections.sort(genres,Filter.getAttribute1Comparator());
        // i sort the year filters by the the year starting from recent.
        Collections.sort(years,Filter.getAttribute2Comparator());
        filtered = true;
    }
    // these are all the filters they are a bunch of simple if statements
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
            explcits.get(1).addTracks(tracks);
        } else {
            explcits.get(0).addTracks(tracks);
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
    // the year one changes a bit as i need to check if the current year has already been made if not i need to make it.
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
    // same with genre where i check if it has already been made.
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
    // if the submit button has been pressed i do another combine then open the next fragment.
    public void submit() {
        MainActivity.tracksHashSet = combining();
        TrackOverview trackOverview = TrackOverview.newInstance();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.root_frame,trackOverview);
        fragmentTransaction.commit();
    }
}
