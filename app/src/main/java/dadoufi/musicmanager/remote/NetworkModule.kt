/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dadoufi.musicmanager.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dadoufi.musicmanager.BuildConfig
import dadoufi.musicmanager.remote.util.AnnotationExclusionStrategy
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Named
import javax.inject.Singleton


@Module
class NetworkModule {


    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        }
    }


    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder().setExclusionStrategies(AnnotationExclusionStrategy()).create()

    @Singleton
    @Provides
    fun provideLastFmInterceptor(@Named("lastfm_api_key") apikey: String) =
        LastFmInterceptor(apikey)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        lastFmInterceptor: LastFmInterceptor,
        @Named("cache") cache: File
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(lastFmInterceptor)
        .addInterceptor(loggingInterceptor)
        .cache(Cache(cache, 10 * 1024 * 1024))
        .build()


    @Singleton
    @Provides
    fun provideLastFmService(
        @Named("base_url") baseUrl: String,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): LastFmService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
        .create(LastFmService::class.java)
}