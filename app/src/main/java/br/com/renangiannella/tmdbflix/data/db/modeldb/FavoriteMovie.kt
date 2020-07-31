package br.com.renangiannella.tmdbflix.data.db.modeldb

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite_movie")
@Parcelize
data class FavoriteMovie(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "user_email")
    val userEmail: String,
    @ColumnInfo(name = "poster")
    val poster_path: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "release")
    val release_date: String,
    @ColumnInfo(name = "genre_id")
    val genre_ids: List<Int>,
    @ColumnInfo(name = "title")
    val original_title: String,
    @ColumnInfo(name = "vote_average")
    val vote_average: String): Parcelable