package net.blusutils.smupe.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import net.blusutils.smupe.data.room.entities.Favorite

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite")
    suspend fun getFavorites(): List<Favorite>

    @Query("SELECT * FROM favorite WHERE id = :id")
    suspend fun getFavoritesByID(id: String): List<Favorite>

    @Query("SELECT * FROM favorite WHERE link = :link")
    suspend fun getFavoritesByLink(link: String): List<Favorite>

    @Query("SELECT * FROM favorite WHERE title = :title")
    suspend fun getFavoritesByTitle(title: String): List<Favorite>

    @Insert
    suspend fun addFavorites(vararg favorites: Favorite)

    @Delete
    suspend fun deleteFavorites(vararg favorites: Favorite)
}