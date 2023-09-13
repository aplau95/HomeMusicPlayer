package com.example.homemusicplayer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.homemusicplayer.api.SearchService
import com.example.homemusicplayer.authManager.AuthAuthenticator
import com.example.homemusicplayer.authManager.AuthInterceptor
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Album
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Artist
import com.example.homemusicplayer.data.apiResponse.mediaTypes.MediaType
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Playlist
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Song
import com.example.homemusicplayer.data.apiResponse.mediaTypes.deserializer.TypeResourceDeserializer
import com.example.homemusicplayer.persist.TokenManager
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(tokenManager: TokenManager): AuthAuthenticator =
        AuthAuthenticator(tokenManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl("https://api.music.apple.com/v1/")
            .addConverterFactory(gsonConverterFactory)

    @Singleton
    @Provides
    fun provideGsonConverterFactor(
        typeResourceDeserializer: TypeResourceDeserializer
    ): GsonConverterFactory {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapterFactory(provideRuntimeTypeAdapterFactory())
        return GsonConverterFactory.create(gsonBuilder.create())
    }


    @Singleton
    @Provides
    fun provideMainAPIService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): SearchService =
        retrofit
            .client(okHttpClient)
            .build()
            .create(SearchService::class.java)

    @Singleton
    @Provides
    fun provideTypeResourceDeserializer(): TypeResourceDeserializer = TypeResourceDeserializer()

    @Singleton
    @Provides
    fun provideRuntimeTypeAdapterFactory() =
        RuntimeTypeAdapterFactory.of(MediaType::class.java, "type", true)
            .registerSubtype(Artist::class.java, "artists")
            .registerSubtype(Song::class.java, "songs")
            .registerSubtype(Album::class.java, "albums")
            .registerSubtype(Playlist::class.java, "playlists")
}

