package uz.context.catapiapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveCat(catEntity: CatEntity)

    @Query("SELECT * FROM CatTable")
    fun getCats(): List<CatEntity>

    @Query("DELETE FROM CatTable")
    fun clearData()
}