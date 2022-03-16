package uz.context.catapiapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CatEntity::class], version = 1)
abstract class CatDatabase : RoomDatabase() {

    abstract fun dao(): CatDao

    companion object {
        private var INSTANCE: CatDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CatDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    CatDatabase::class.java, "cat.db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}