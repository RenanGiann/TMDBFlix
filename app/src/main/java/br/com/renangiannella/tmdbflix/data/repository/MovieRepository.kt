package br.com.renangiannella.tmdbflix.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import br.com.renangiannella.tmdbflix.data.db.TMDBFlixDataBase
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.dao.FavoriteDAO
import br.com.renangiannella.tmdbflix.data.db.modeldb.dao.UserDAO
import br.com.renangiannella.tmdbflix.data.db.modeldb.dao.WatchDAO
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.network.APIService
import retrofit2.Response

class MovieRepository(context: Context) {

    private val userDAO: UserDAO by lazy {
        TMDBFlixDataBase.getDB(context).userDAO()
    }

    private val favoriteDAO: FavoriteDAO by lazy {
        TMDBFlixDataBase.getDB(context).favoriteDAO()
    }

    private val watchDAO: WatchDAO by lazy {
        TMDBFlixDataBase.getDB(context).watchDAO()
    }

    suspend fun insertUser(user: User) {
        userDAO.insertUser(user)
    }

    fun getUser(email: String, password: String): LiveData<User> = userDAO.getUser(email, password)

    fun getUserByEmail(email: String): LiveData<User> = userDAO.getUserByEmail(email)

    suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovie) {
        favoriteDAO.insertMovie(favoriteMovie)
    }

    fun getFavoriteMovie(userEmail: String) : LiveData<List<FavoriteMovie>> = favoriteDAO.getFavoriteMovie(userEmail)

    suspend fun deleteFavoriteMovie(favoriteMovie: FavoriteMovie) {
        favoriteDAO.deleteFavoriteMovie(favoriteMovie)
    }

    suspend fun deleteUser(user: User) {
        userDAO.deleteUser(user)
    }

    fun getWatchMovie(userEmail: String): LiveData<List<WatchMovie>> = watchDAO.getWatchMovie(userEmail)

    suspend fun insertWatchMovie(watchMovie: WatchMovie) = watchDAO.insertMovieWatch(watchMovie)

    suspend fun deleteWatchMovie(watchMovie: WatchMovie) = watchDAO.deleteWatchMovie(watchMovie)

    suspend fun getMoviePopular(apiKey: String, language: String) = APIService.service.getMoviesPopular(apiKey, language)
    suspend fun getMovieGenre(apiKey: String, language: String, genre: Int) = APIService.service.getMoviesGenre(apiKey, language, genre)
    suspend fun searchMovies(query: String, apiKey: String, language: String): MovieResponse = APIService.service.searchMovies(query, apiKey, language)
    suspend fun getTopRatedMovies(apiKey: String, language: String): Response<MovieResponse> = APIService.service.getTopRatedMovies(apiKey, language)
}

