package uz.context.catapiapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CatTable")
data class CatEntity(

    @PrimaryKey
    var id: String,
    val url: String
)