package br.com.renangiannella.tmdbflix.data.repository

import br.com.renangiannella.tmdbflix.data.network.APIService

class MovieRepository {

//    fun getMoviePopular(apiKey: String, language: String): Response<MovieResponse> {
//        return APIService.service.getMoviesPopular(apiKey, language)
//    }

    suspend fun getMoviePopular(apiKey: String, language: String) = APIService.service.getMoviesPopular(apiKey, language)

}