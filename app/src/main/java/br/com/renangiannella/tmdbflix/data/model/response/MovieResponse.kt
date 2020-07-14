package br.com.renangiannella.tmdbflix.data.model.response

import android.os.Parcelable
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieResponse(

    val results: List<MovieResult>

): Parcelable