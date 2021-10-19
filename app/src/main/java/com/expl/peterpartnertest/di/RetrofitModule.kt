package com.expl.peterpartnertest.di

import com.expl.peterpartnertest.network.cbr_api.CbrRemoteService
import com.expl.peterpartnertest.network.users_api.UsersRemoteService
import com.expl.peterpartnertest.utilits.BASE_URL_CBR
import com.expl.peterpartnertest.utilits.BASE_URL_USERS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    fun provideBaseInterceptor() :Interceptor = invoke { chain ->
        val newUrl = chain
            .request()
            .url
            .newBuilder()
            .build()
        val request = chain
            .request()
            .newBuilder()
            .url(newUrl)
            .build()
        return@invoke chain.proceed(request)
    }

    @Provides
    fun provideOkHttpClient(baseInterceptor: Interceptor):OkHttpClient {
        val loggingInterceptor = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

        return OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(baseInterceptor)
        .build()
    }

    @Provides
    @Singleton
    fun provideUsersRemoteService(client: OkHttpClient): UsersRemoteService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_USERS)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(UsersRemoteService::class.java)
    }

    @Provides
    @Singleton
    fun provideCbrRemoteService(client: OkHttpClient): CbrRemoteService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_CBR)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CbrRemoteService::class.java)
    }
}