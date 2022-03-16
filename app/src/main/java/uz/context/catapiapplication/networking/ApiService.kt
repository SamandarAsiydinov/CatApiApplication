package uz.context.catapiapplication.networking

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import uz.context.catapiapplication.model.Breeds
import uz.context.catapiapplication.model.CatList
import uz.context.catapiapplication.model.MyCats

@JvmSuppressWildcards
interface ApiService {

    @GET("images/search?")
    fun getAllList(@Query("limit") limit: Int, @Query("page") page: Int, @Query("order") order: String): Call<ArrayList<CatList>>

    @Multipart
    @POST("images/upload")
    fun uploadFile(@Part image: MultipartBody.Part, @Part("sub_id") name: String): Call<CatList>

    @GET("breeds")
    fun getAllBreeds(): Call<ArrayList<Breeds>>

    @GET("search")
    fun search(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("mimi_types") type: String
    ): Call<ArrayList<CatList>>

    @GET("images/search?")
    fun searchWithBreed(@Query("breeds_ids=siam") breedsId: String): Call<ArrayList<Breeds>>

    @GET("images")
    fun getMyCats(
        @Query("page") page: Int,
        @Query("limit") per_page: Int = 30
    ): Call<ArrayList<MyCats>>
}