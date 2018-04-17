package kareemahmed.spotifyxtremeedition;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Playlists> moviesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, trackNumber;
        public ImageView image;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.Title);
            trackNumber = (TextView) view.findViewById(R.id.amountOfSongs);
            image = (ImageView) view.findViewById(R.id.AlbumCover);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout);

        }
    }

    public MoviesAdapter(List<Playlists> moviesList , Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Playlists movie = moviesList.get(position);
        holder.name.setText(movie.getName());
        holder.trackNumber.setText(movie.getTrackNumber());
        Picasso.with(context)
                .load(movie.getImage())
                .resize(300,300)
                .into(holder.image);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("Parcel Data", movie);
                        AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                        SubCategoryFragment subCategoryFragment = SubCategoryFragment.newInstance();
                        subCategoryFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.root_frame,subCategoryFragment);
                        fragmentTransaction.commit();
            }
        });
    }
    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

