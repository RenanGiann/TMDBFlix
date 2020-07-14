package br.com.renangiannella.tmdbflix.data.model.result

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResult(

    val id: Int,
    val poster_path: String,
    val overview: String,
    val release_date: String,
    val genre_ids: List<Int>,
    val original_title: String,
    val vote_average: String

): Parcelable