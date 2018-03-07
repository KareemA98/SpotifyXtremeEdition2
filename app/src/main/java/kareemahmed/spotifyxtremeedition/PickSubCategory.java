package kareemahmed.spotifyxtremeedition;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PickSubCategory extends Activity {
public TextView genre;
public Playlists movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_sub_category);
        movie = getIntent().getParcelableExtra("Parcel Data");
        genre = findViewById(R.id.Genre);
    }
    public void sendMessage(View view) {
        System.out.println(movie.trackList.get(0).getName());
    }
}
