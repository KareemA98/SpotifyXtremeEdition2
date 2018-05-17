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
import java.util.List;
// This fragment has an recycler view which displays all the playlists in the users account
public class PlaylistFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView recyclerView;
    private PlaylistAdapter mAdapter;

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
        // this is me making the loader
        getLoaderManager().initLoader(0,null , this);
        mAdapter = new PlaylistAdapter(getActivity());
        recyclerView.setHasFixedSize(true);
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        // adding custom divider line with padding 16dp
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // this is called when the loader is made. it says what data we should send the fragment.
        Uri baseUri = Uri.parse("content://kareemahmed.spotifyxtremeedition.PlaylistProvider/playlists");
        // this is a list of the columns i want to return
        String[] projection = new String[]{PlaylistProvider.COLUMN_IMAGE, PlaylistProvider.COLUMN_NAME, PlaylistProvider.COLUMN_NOOFSONGS, PlaylistProvider.COLUMN_ID, PlaylistProvider.COLUMN_USERID};
        return new CursorLoader(getActivity(), baseUri,
                projection , null , null, null);
    }
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setData(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setData(null);
    }
}
