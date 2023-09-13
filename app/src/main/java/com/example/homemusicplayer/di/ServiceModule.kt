package com.example.homemusicplayer.di

import android.content.Context
import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.TransferListener
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import java.io.File

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun provideAudioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

    @Provides
    @ServiceScoped
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .build()
        .apply {
            setAudioAttributes(audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
        }

    @UnstableApi
    class DataSource : HttpDataSource {

        override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
            TODO("Not yet implemented")
        }

        override fun addTransferListener(transferListener: TransferListener) {

        }

        override fun open(dataSpec: DataSpec): Long {
            TODO("Not yet implemented")
        }

        override fun getUri(): Uri? {
            TODO("Not yet implemented")
        }

        override fun getResponseHeaders(): MutableMap<String, MutableList<String>> {
            TODO("Not yet implemented")
        }

        override fun close() {
            TODO("Not yet implemented")
        }

        override fun setRequestProperty(name: String, value: String) {
            TODO("Not yet implemented")
        }

        override fun clearRequestProperty(name: String) {
            TODO("Not yet implemented")
        }

        override fun clearAllRequestProperties() {
            TODO("Not yet implemented")
        }

        override fun getResponseCode(): Int {
            TODO("Not yet implemented")
        }

    }

    @UnstableApi
    class asdf : HttpDataSource.Factory {

        override fun createDataSource(): HttpDataSource {
            return DataSource()
        }

        override fun setDefaultRequestProperties(defaultRequestProperties: MutableMap<String, String>): HttpDataSource.Factory {
            return this
        }
    }


    @Provides
    @ServiceScoped
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun provideDataSourceFactory(
        @ApplicationContext context: Context,
        dataSource: HttpDataSource.Factory
    ): CacheDataSource.Factory {

        val cacheDir = File(context.cacheDir, "media")
        val databaseProvider = StandaloneDatabaseProvider(context)

        val cache = SimpleCache(cacheDir, NoOpCacheEvictor(), databaseProvider)
        return CacheDataSource.Factory().apply {
            setCache(cache)
            setUpstreamDataSourceFactory(dataSource)
        }
    }

}