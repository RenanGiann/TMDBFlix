package br.com.renangiannella.tmdbflix.data.db.modeldb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie

@Dao
interface WatchDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieWatch(watchMovie: WatchMovie)

    @Query("SELECT * FROM watch_movie WHERE user_email = :userEmail")
    fun getWatchMovie(userEmail: String) : LiveData<List<WatchMovie>>

    @Delete
    suspend fun deleteWatchMovie(watchMovie: WatchMovie)

}