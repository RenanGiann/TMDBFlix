package br.com.renangiannella.tmdbflix.data.db.modeldb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.renangiannella.tmdbflix.data.db.modeldb.User

@Dao
interface UserDAO {

    //Inserir um objeto usuario
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    //* isso é igual a tudo
    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): LiveData<User>

    @Query("SELECT * FROM user WHERE email = :email")
    fun getUserByEmail(email: String): LiveData<User>

    //Delete user
    @Delete
    suspend fun deleteUser(user: User)
}