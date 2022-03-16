package uz.context.catapiapplication.model

import com.google.gson.annotations.SerializedName

data class Breeds(
    val id: String,
    @field:SerializedName("name")
    val name: String,
    val origin: String,
    val description: String,
    @field:SerializedName("wikipedia_url")
    val wikipedia_url: String,
    val image: Image
)
data class Image(
    val id: String,
    val url: String
)