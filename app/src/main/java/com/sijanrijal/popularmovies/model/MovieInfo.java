package com.sijanrijal.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfo implements Parcelable{
    public final int id;
    public final String title;
    public final double vote_average;
    public final String release_date;
    public final String overview;
    public final String poster_path;
    public final int[] genre_ids;

    protected MovieInfo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        vote_average = in.readDouble();
        release_date = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        genre_ids = in.createIntArray();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(vote_average);
        dest.writeString(release_date);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeIntArray(genre_ids);
    }
}
