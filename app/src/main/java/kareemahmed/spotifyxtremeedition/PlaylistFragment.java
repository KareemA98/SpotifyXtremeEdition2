package kareemahmed.spotifyxtremeedition;


import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class PlaylistFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private List<Playlists> playlistlist = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;

    public static PlaylistFragment newInstance(){
        PlaylistFragment playlistFragment = new PlaylistFragment();
        return playlistFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.playlist_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
         recyclerView = getView().findViewById(R.id.playlistRecyclerView);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.playlistRecyclerView);
        getLoaderManager().initLoader(0,null , this);
        mAdapter = new MoviesAdapter(playlistlist, getActivity());
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
        recyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse("content://kareemahmed.spotifyxtremeedition.ExampleProvider/playlists");
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String[] projection = new String[]{ExampleProvider.COLUMN_IMAGE,ExampleProvider.COLUMN_NAME,ExampleProvider.COLUMN_NOOFSONGS,ExampleProvider.COLUMN_ID,ExampleProvider.COLUMN_USERID};
        return new CursorLoader(getActivity(), baseUri,
                projection , null , null, null);
    }
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.setData(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.setData(null);
    }
}
