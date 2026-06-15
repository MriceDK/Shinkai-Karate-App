package be.mauricedeke.shinkai.di

import be.mauricedeke.shinkai.BuildConfig
import be.mauricedeke.shinkai.data.remote.AuthTokenStore
import be.mauricedeke.shinkai.data.remote.SessionEventBus
import be.mauricedeke.shinkai.data.remote.api.AuthApi
import be.mauricedeke.shinkai.data.remote.api.BeltApi
import be.mauricedeke.shinkai.data.remote.api.EventApi
import be.mauricedeke.shinkai.data.remote.api.KataApi
import be.mauricedeke.shinkai.data.remote.api.LexiconApi
import be.mauricedeke.shinkai.data.remote.api.StrengthApi
import be.mauricedeke.shinkai.data.remote.api.SupportApi
import be.mauricedeke.shinkai.data.remote.api.TrainingApi
import be.mauricedeke.shinkai.data.remote.api.TrainingSessionApi
import be.mauricedeke.shinkai.data.remote.api.UserApi
import be.mauricedeke.shinkai.data.remote.api.VersionsApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenStore: AuthTokenStore,
        sessionEventBus: SessionEventBus
    ): OkHttpClient =
        OkHttpClient.Builder()
            .certificatePinner(
                CertificatePinner.Builder()
                    // Leaf certificate — shinkai.ktsd.dscloud.me
                    .add(
                        "shinkai.ktsd.dscloud.me",
                        "sha256/j8IW9C5CvzfSXbKo7CSKqBpqOpsIgft7Bdkg2BqUn4o="
                    )
                    // Intermediate CA — Let's Encrypt E7 (backup pin: survives leaf renewal)
                    .add(
                        "shinkai.ktsd.dscloud.me",
                        "sha256/y7xVm0TVJNahMr2sZydE2jQH8SquXV9yLF9seROHHHU="
                    )
                    .build()
            )
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = tokenStore.accessToken
                    ?.let {
                        chain.request().newBuilder().header("Authorization", "Bearer $it").build()
                    }
                    ?: chain.request()
                val response = chain.proceed(request)
                if (response.code == 401) {
                    tokenStore.accessToken = null
                    sessionEventBus.notifySessionExpired()
                }
                response
            }
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides @Singleton
    fun provideEventApi(retrofit: Retrofit): EventApi = retrofit.create(EventApi::class.java)

    @Provides @Singleton
    fun provideTrainingApi(retrofit: Retrofit): TrainingApi = retrofit.create(TrainingApi::class.java)

    @Provides @Singleton
    fun provideTrainingSessionApi(retrofit: Retrofit): TrainingSessionApi = retrofit.create(TrainingSessionApi::class.java)

    @Provides @Singleton
    fun provideBeltApi(retrofit: Retrofit): BeltApi = retrofit.create(BeltApi::class.java)

    @Provides @Singleton
    fun provideKataApi(retrofit: Retrofit): KataApi = retrofit.create(KataApi::class.java)

    @Provides @Singleton
    fun provideLexiconApi(retrofit: Retrofit): LexiconApi = retrofit.create(LexiconApi::class.java)

    @Provides @Singleton
    fun provideStrengthApi(retrofit: Retrofit): StrengthApi = retrofit.create(StrengthApi::class.java)

    @Provides @Singleton
    fun provideSupportApi(retrofit: Retrofit): SupportApi = retrofit.create(SupportApi::class.java)

    @Provides @Singleton
    fun provideVersionsApi(retrofit: Retrofit): VersionsApi = retrofit.create(VersionsApi::class.java)
}
