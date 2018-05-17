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

/**
 * Created by krimb on 23/03/2018.
 * This is the recycler view adapter for my Filters
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    // the view takes in a list of Filters
    private List<Filter> genreList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // this is creation of a row in the recycler view
        public TextView title, numberOfSongs;
        public CheckBox checkBox;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            // this is where all the parts of my row are assigned
            title = (TextView) view.findViewById(R.id.FilterName);
            numberOfSongs = (TextView) view.findViewById(R.id.filterSongs);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.filterRelativelayout);
        }
    }

    public FilterAdapter(List<Filter> genreList) {
        this.genreList = genreList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // This assigns values to all parts of the row
        final Filter filter = genreList.get(position);
        holder.title.setText(filter.getName());
        holder.numberOfSongs.setText(filter.getSize());
        // ive added a listener to the check box
        holder.checkBox.setOnCheckedChangeListener(null);
        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(filter.isSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //if the checkbox gets pressed then it will change the checkbox
                filter.setChecked(isChecked);
            }
        });
    }
    @Override
    public int getItemCount() {
        return genreList.size();
    }
}

