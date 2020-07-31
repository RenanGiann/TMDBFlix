package br.com.renangiannella.tmdbflix.data.network

import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET ("movie/popular")
    suspend fun getMoviesPopular(@Query("api_key") apiKey:String,
                                 @Query("language") language: String)
            : Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getMoviesGenre(@Query("api_key") apiKey: String,
                               @Query("language") language: String,
                               @Query("with_genres") genre: Int)
            : Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String,
                             @Query("api_key") apiKey: String,
                             @Query("language") language: String)
            : MovieResponse

}