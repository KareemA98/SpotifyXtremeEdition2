package kareemahmed.spotifyxtremeedition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PickSubCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_sub_category);
        Playlists movie = getIntent().getParcelableExtra("Parcel Data");
    }
}
