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
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    private List<Filter> genreList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, numberOfSongs;
        public CheckBox checkBox;
        public RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.FilterName);
            numberOfSongs = (TextView) view.findViewById(R.id.filterSongs);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.filterRelativelayout);
        }
    }

    public FilterAdapter(List<Filter> genreList , Context context) {
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
        final Filter genre = genreList.get(position);
        holder.title.setText(genre.getName());
        holder.numberOfSongs.setText(genre.getSize());
        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(genre.isSelected());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                genre.setChecked(isChecked);
            }
        });
    }
    @Override
    public int getItemCount() {
        return genreList.size();
    }
}

