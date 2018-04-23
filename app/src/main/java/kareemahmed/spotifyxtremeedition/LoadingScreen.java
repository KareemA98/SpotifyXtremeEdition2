package kareemahmed.spotifyxtremeedition;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingScreen {
    private static ProgressDialog progress;
    private static double seconds;

    public LoadingScreen(Context context , double seconds){
        this.seconds = seconds;
        progress = new ProgressDialog(context);
        progress.setCancelable(true);
        progress.setMessage("File downloading ...");
        progress.show();
    }

    public static void increment(){
        seconds--;
        if (seconds == 0){
            progress.dismiss();
        }
    }
}
