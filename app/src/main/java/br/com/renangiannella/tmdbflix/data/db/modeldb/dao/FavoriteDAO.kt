package br.com.renangiannella.tmdbflix.data.db.modeldb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie

@Dao
interface FavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(favoriteMovies: FavoriteMovie)

    @Query("SELECT * FROM favorite_movie WHERE user_email = :userEmail")
    fun getFavoriteMovie(userEmail: String) : LiveData<List<FavoriteMovie>>

    @Delete
    suspend fun deleteFavoriteMovie(favoriteMovies: FavoriteMovie)
}