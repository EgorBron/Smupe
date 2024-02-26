package net.blusutils.smupe.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import net.blusutils.smupe.data.room.dao.FavoritesDao
import net.blusutils.smupe.data.room.entities.Favorite
// TODO: https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we
@Database(entities = [Favorite::class], version = 13)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favesDao(): FavoritesDao
    companion object {
        lateinit var instance: AppDatabase
        lateinit var favesDao: FavoritesDao
        fun initDatabase(
            ctx: Context,
            name: String = "smupe_db",
            migrations: List<Migration>? = null
        ) {
            instance = Room
                .databaseBuilder(ctx, AppDatabase::class.java, name)
                .apply {
                    migrations?.let { addMigrations(*it.toTypedArray()) }
                        ?: fallbackToDestructiveMigration()
                }
                .build()
            favesDao = instance.favesDao()
        }
        fun Context.initDb(
            name: String = "smupe_db",
            migrations: List<Migration>? = null
        ) {
            initDatabase(this, name, migrations)
        }
    }
}
