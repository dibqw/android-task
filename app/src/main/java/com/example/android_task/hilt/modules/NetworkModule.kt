package com.example.android_task.hilt.modules

import com.example.android_task.data.services.ApiServices
import com.example.android_task.utils.AuthInterceptor
import com.example.android_task.utils.AuthTokenProvider
import com.example.android_task.utils.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.baubuddy.de/dev/"

    const val AUTH_OKHTTP_CLIENT = "AuthOkHttpClient"
    const val AUTH_RETROFIT = "AuthRetrofit"

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        // Inject the auth-specific ApiService
        @Named(AUTH_RETROFIT) authApiService: ApiServices,
        authTokenProvider: AuthTokenProvider
    ): TokenAuthenticator {
        return TokenAuthenticator(authApiService, authTokenProvider)
    }

    @Provides
    @Singleton
    @Named(AUTH_OKHTTP_CLIENT)
    fun provideAuthOkHttpClient(): OkHttpClient {
        // This client should not have the TokenAuthenticator to avoid loops
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenAuthenticator: TokenAuthenticator, authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    @Named(AUTH_RETROFIT)
    fun provideAuthRetrofit(@Named(AUTH_OKHTTP_CLIENT) okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    @Provides
    @Singleton
    @Named(AUTH_RETROFIT) // Provide the ApiService using the auth Retrofit instance
    fun provideAuthApiService(@Named(AUTH_RETROFIT) retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }
}