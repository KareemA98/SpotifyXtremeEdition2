package kareemahmed.spotifyxtremeedition;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by krimb on 21/03/2018.
 */

public class YearsAdapter extends RecyclerView.Adapter<YearsAdapter.MyViewHolder> {

    private List<Years> genreList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView genre, genreSongs;
        public CheckBox checkBox;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            genre = (TextView) view.findViewById(R.id.filterTitle);
            genreSongs = (TextView) view.findViewById(R.id.filterSongs);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.filterRelativelayout);
        }
    }

    public YearsAdapter(List<Years> genreList, Context context) {
        this.genreList = genreList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Years genre = genreList.get(position);
        holder.genre.setText(genre.getName());
        holder.genreSongs.setText(genre.getSize());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }
}
