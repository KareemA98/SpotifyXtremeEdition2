package kareemahmed.spotifyxtremeedition;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebViewFragment extends Fragment {

    public static WebViewFragment newInstance(){
        WebViewFragment webViewFragment= new WebViewFragment();
        return webViewFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.webview, container, false);
        WebView mywebview = (WebView) v.findViewById(R.id.webview);
        String data = userGuide();
        mywebview.loadData(data, "text/html", "UTF-8");
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }
    public String userGuide() {
        String data =
                "<html>"+
                "<body>"+
                        "<h1>Spotify Xtreme Edition</h1>" +
                        "<h1>User Guide</h1>"+
                       "<h2>Overview</h2>" +
                        "<p>The purpose of this app is very simple. It allows you to take your Spotify&nbsp;playlists and filter them in many different ways to create new playlists.</p>" +
                        "<h2>Authentication</h2>" +
                        "<p><img src='https://i.imgur.com/tHiQiPX.png' width='147' height='262' align='right' /></p>" +
                        "<p>The first stage of my app is authentication with Spotify. This means to use this app you need to have a Spotify&nbsp;account or else you won't have any playlists to filter. You can sign in using your Facebook or Spotify details.</p>" +
                        "<h2>Picking Playlist</h2>" +
                        "<p>The app will display all of your created and followed playlists that are connected to your account. For each playlist it will display&nbsp;the name, the image associated with it and the number of songs included.&nbsp;</p>" +
                        "<h2>Filtering</h2>" +
                        "<p>There are many ways to filter your playlist, it's important to remember when filtering that you&nbsp;can always check the number of songs that are associated with your current filter by pressing the update button. Within a filter is additive whereas between filters is subtractive. what this means is if you pick songs from 2012 and 2013 you will get a combination of songs from those years. where if you have 2013, 2012 and rock you will get only songs that came out in 2012 or 2013 that are rock songs.</p>" +
                        "<h2>Checking Songs</h2>" +
                        "<p>After you press Next you'll be brought to a screen that gives you a list of all the tracks that are associated with your filter. this allows you to check if they are the songs&nbsp;you're looking for. Important to note that if you do no filters you'll&nbsp;just get the original playlist.</p>" +
                        "<h2>Creating a playlist</h2>"+
                        "<p>When you press Submit a small dialogue will pop up allowing you to choose the name of your new playlist. You can name your playlist whatever even the same as some playlists you already have. Once you press ok the Spotify&nbsp;app will open and allow you to start playing immediately.</p>" +
                        "<h2>Troubleshooting</h2>" +
                        "<p>1. Most all issues in the app are fixed with fully closing the app and relaunching it.</p>" +
                        "<p>2. Make sure you have a good and stable internet connection as then application does a lot of internet transactions</p>" +
                        "</body>" +
                        "</html>";

        return data;
    }


}
