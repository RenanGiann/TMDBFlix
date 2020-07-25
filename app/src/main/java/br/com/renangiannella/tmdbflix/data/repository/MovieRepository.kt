package br.com.renangiannella.tmdbflix.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import br.com.renangiannella.tmdbflix.data.db.TMDBFlixDataBase
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.db.modeldb.dao.FavoriteDAO
import br.com.renangiannella.tmdbflix.data.db.modeldb.dao.UserDAO
import br.com.renangiannella.tmdbflix.data.network.APIService

class MovieRepository(context: Context) {

    private val userDAO: UserDAO by lazy {
        TMDBFlixDataBase.getDB(context).userDAO()
    }

    private val favoriteDAO: FavoriteDAO by lazy {
        TMDBFlixDataBase.getDB(context).favoriteDAO()
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

    suspend fun getMoviePopular(apiKey: String, language: String) = APIService.service.getMoviesPopular(apiKey, language)
    suspend fun getMovieGenre(apiKey: String, language: String, genre: Int) = APIService.service.getMoviesGenre(apiKey, language, genre)
}

