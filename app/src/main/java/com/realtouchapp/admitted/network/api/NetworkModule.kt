package com.realtouchapp.admitted.network.api

import android.util.Log
import com.realtouchapp.admitted.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
//        val interceptor = HttpLoggingInterceptor()
        val interceptor = HttpLoggingInterceptor { message ->
            if (BuildConfig.DEBUG) {
                Log.d("HttpLoggingInterceptor", message)
            }
        }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor;
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(com.realtouchapp.admitted.network.api.AdmittedApi.SERVER_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}