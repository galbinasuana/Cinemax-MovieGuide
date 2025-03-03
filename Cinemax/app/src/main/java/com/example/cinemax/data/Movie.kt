package com.example.cinemax.data

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val title: String,
    val overview: String,
    val genres: String,
    val releaseDate: String,
    val posterPath: String,
    val avgRating: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(overview)
        parcel.writeString(genres)
        parcel.writeString(releaseDate)
        parcel.writeString(posterPath)
        parcel.writeDouble(avgRating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}