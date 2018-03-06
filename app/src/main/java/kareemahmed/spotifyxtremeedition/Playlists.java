package kareemahmed.spotifyxtremeedition;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by krimb on 01/03/2018.
 */

public class Playlists implements Parcelable {
    private String name,trackNumber,image,id;
    public Playlists() {
    }

    public Playlists(String name, String trackNumber, String image , String id) {
        this.name = name;
        this.trackNumber = trackNumber;
        this.image = image;
        this.id = id;
    }

    protected Playlists(Parcel in) {
        name = in.readString();
        trackNumber = in.readString();
        image = in.readString();
        id = in.readString();
    }

    public static final Creator<Playlists> CREATOR = new Creator<Playlists>() {
        @Override
        public Playlists createFromParcel(Parcel in) {
            return new Playlists(in);
        }

        @Override
        public Playlists[] newArray(int size) {
            return new Playlists[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(trackNumber);
        parcel.writeString(image);
        parcel.writeString(id);
    }
}
