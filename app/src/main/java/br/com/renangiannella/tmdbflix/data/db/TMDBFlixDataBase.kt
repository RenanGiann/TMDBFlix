package br.com.renangiannella.tmdbflix.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.renangiannella.tmdbflix.data.db.converter.Converters
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.User
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.dao.FavoriteDAO
import br.com.renangiannella.tmdbflix.data.db.modeldb.dao.UserDAO
import br.com.renangiannella.tmdbflix.data.db.modeldb.dao.WatchDAO

@Database(entities = [User::class, FavoriteMovie::class, WatchMovie::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TMDBFlixDataBase: RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun favoriteDAO(): FavoriteDAO
    abstract fun watchDAO(): WatchDAO

    companion object {
        @Volatile
        private var INSTANCE: TMDBFlixDataBase? = null

        val MIGRATION_1_2: Migration = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
//               Incluir comando SQL para atualizacao do banco
            }

        }

        fun getDB(context: Context): TMDBFlixDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TMDBFlixDataBase::class.java,
                    "tmdb_db")
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}