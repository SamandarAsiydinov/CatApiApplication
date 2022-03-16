package uz.context.catapiapplication.networking

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient(var context: Context) {

    var BASE_URl = "https://api.thecatapi.com/v1/"

    private val client = getClient()
    private val retrofit = getRetrofit(client)


    private fun getRetrofit(client: OkHttpClient): Retrofit {

        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URl)
            .client(client)
            .build()
    }

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.header("x-api-key", "cd115401-446a-4694-b133-c558324ab265")
            chain.proceed(builder.build())
        })
        .build()

    fun <T> createServiceWithAuth(service: Class<T>?): T {
        val newClient =
            client.newBuilder().addInterceptor(Interceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                request = builder.build()
                chain.proceed(request)
            }).build()
        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service!!)
    }
}