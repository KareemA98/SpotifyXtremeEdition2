package kareemahmed.spotifyxtremeedition;

import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by krimb on 01/03/2018.
 */

public class Playlists {
    private String name,trackNumber,image,id;
    public Playlists() {
    }

    public Playlists(String name, String trackNumber, String image , String id) {
        this.name = name;
        this.trackNumber = trackNumber;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id = id;
    }


}
