package cvut.fit.kot.data.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideLogger(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides @Singleton
    fun provideOkHttp(
        logger: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(authInterceptor)   // «Bearer <jwt>»
        .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /* ---------- high-level ---------- */

    @Provides fun provideAuthApi(r: Retrofit): AuthApi         = r.create(AuthApi::class.java)
    @Provides fun provideClientApi(r: Retrofit): ClientApi     = r.create(ClientApi::class.java)
    @Provides fun provideFileApi(r: Retrofit): FileApi         = r.create(FileApi::class.java)
    @Provides fun provideLocationApi(r: Retrofit): LocationApi = r.create(LocationApi::class.java)
    @Provides fun provideSearchApi(r: Retrofit): SearchApi = r.create(SearchApi::class.java)
}
