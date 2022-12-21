package com.robivan.binlist.data.api

import com.robivan.binlist.data.api.model.ResponseDTO
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{number}")
    fun getCardDetails(
        @Path("number") number: String
    ) : Single<ResponseDTO>

    companion object {
        private const val BASE_URL = "https://lookup.binlist.net/"
        fun getInstance(): ApiService =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(OkHttpClient().newBuilder().apply {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).takeIf { BuildConfig.DEBUG }
                }.build())
                .build()
                .create(ApiService::class.java)
    }
}