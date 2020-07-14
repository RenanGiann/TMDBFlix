package br.com.renangiannella.tmdbflix.data.network

import com.google.gson.GsonBuilder
import br.com.renangiannella.tmdbflix.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIService {

    private fun initRetrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()  //as vezes o json pode quebrar, o leniente faz esse tratamento
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) //intercepta as chamadas de json, mostrando em console o log

        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    }

    val service: Service = initRetrofit().create(Service::class.java)
}