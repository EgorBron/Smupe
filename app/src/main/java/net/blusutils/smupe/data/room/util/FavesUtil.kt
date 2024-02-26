package net.blusutils.smupe.data.room.util

import net.blusutils.smupe.data.room.AppDatabase
import net.blusutils.smupe.data.room.entities.Favorite
import net.blusutils.smupe.util.calculateRoomResourceId

/**
 * Helper class used as shorthand for [net.blusutils.smupe.data.room.AppDatabase.favesDao]
 */
object FavesUtil {

    /**
     * Tries to add or remove resource from favorites.
     * @param fave any data to identify resource as unique
     * @return whether resource added to favorites or not
     */
    suspend fun tryAddFave(fave: String): Boolean {
        val id = calculateRoomResourceId(fave)
        return tryAddFave(
            Favorite(
                id,
                fave,
                null,
                //null // TODO
            )
        )
    }

    /**
     * Tries to add or remove resource from favorites.
     * @param fave resource to process
     * @return whether resource added to favorites or not
     * @see tryAddFave(String)
     */
    suspend fun tryAddFave(fave: Favorite): Boolean {
        return if (AppDatabase.favesDao.getFavoritesByID(fave.id).isNotEmpty()) {
            AppDatabase.favesDao.deleteFavorites(fave)
            false
        } else {
            AppDatabase.favesDao.addFavorites(fave)
            true
        }
    }

    /**
     * Gets all favorites. Most useless method, BTW.
     * @return list of all favorites
     */
    suspend fun getFaves(): List<Favorite> {
        return AppDatabase.favesDao.getFavorites()
    }

    /**
     * Safely tries to find favorite by ID. Another useless method.
     * @param id resource ID (basically SHA-1 hash)
     * @return whether resource exist in favorites or not
     */
    suspend fun getFaveById(id: String): Boolean {
        return AppDatabase.favesDao.getFavoritesByID(
            calculateRoomResourceId(id)
        ).isNotEmpty()
    }
}