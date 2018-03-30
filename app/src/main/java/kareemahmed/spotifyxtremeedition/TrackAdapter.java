package kareemahmed.spotifyxtremeedition;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

/**
 * Created by krimb on 24/03/2018.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.MyViewHolder> {
    private List<Tracks> list;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist , album;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.TrackName);
            artist = (TextView) view.findViewById(R.id.ArtistName);
            album = (TextView) view.findViewById(R.id.AlbumName);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.trackrelativelayout);
        }
    }

    public TrackAdapter(List<Tracks> list , Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public TrackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_list_row, parent, false);

        return new TrackAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrackAdapter.MyViewHolder holder, int position) {
        final Tracks track = list.get(position);
        holder.title.setText(track.getName());
        holder.artist.setText(track.getArtistName());
        holder.album.setText(track.getAlbumName());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
