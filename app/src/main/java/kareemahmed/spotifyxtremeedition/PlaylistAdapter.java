package kareemahmed.spotifyxtremeedition;

import android.content.Context;
import android.database.Cursor;
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

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {
    //this is an adapter for my recycler view. this view contains all the info about each playlist

    private Context context;
    private Cursor mCursor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, trackNumber;
        private ImageView image;
        private RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.Title);
            trackNumber = (TextView) view.findViewById(R.id.amountOfSongs);
            image = (ImageView) view.findViewById(R.id.AlbumCover);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout);

        }
    }

    public PlaylistAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // i take in a cursor with all the database info so here i attach the data it to the recyclerview here

        String word = "";
        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                int indexWord = mCursor.getColumnIndex(PlaylistProvider.COLUMN_NAME);
                word = mCursor.getString(indexWord);
                holder.name.setText(word);
                indexWord = mCursor.getColumnIndex(PlaylistProvider.COLUMN_NOOFSONGS);
                word = mCursor.getString(indexWord);
                holder.trackNumber.setText(word +" songs");
            }
        }
        // i use the picasso library to easily get images to put in my app.
        Picasso.with(context)
                .load(mCursor.getString(mCursor.getColumnIndex(PlaylistProvider.COLUMN_IMAGE)))
                .resize(300,300)
                .into(holder.image);
        // Here i create an onclick listener for the list which will open a new fragment
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // im passing this fragment the playlist which was selected.
                        Bundle bundle = new Bundle();
                        mCursor.moveToPosition(position);
                        Playlists playlist = new Playlists(mCursor);
                        bundle.putParcelable("Parcel Data", playlist);
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
        if (mCursor != null){
            return mCursor.getCount();
        } else{
            return - 1;
        }
    }
    public void setData(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }
}

