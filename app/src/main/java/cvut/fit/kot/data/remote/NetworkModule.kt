package cvut.fit.kot.data.remote

import com.google.gson.Gson
import cvut.fit.kot.data.local.SessionDataStore
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

    private const val BASE_HTTP_URL = "http://10.0.2.2:8080/"
    private const val BASE_WS_URL   = "ws://10.0.2.2:8080"

    /* ---------- OkHttp ---------- */

    @Provides @Singleton
    fun provideLogger(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides @Singleton
    fun provideHttpClient(
        logger: HttpLoggingInterceptor,
        auth:   AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(auth)
        .build()

    @Provides @Singleton @Named("WsClient")
    fun provideWsClient(auth: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(auth)
            .build()

    @Provides @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        gson:   Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_HTTP_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides @Singleton fun provideGson(): Gson = Gson()

    @Provides fun provideAuthApi(r: Retrofit): AuthApi         = r.create(AuthApi::class.java)
    @Provides fun provideClientApi(r: Retrofit): ClientApi     = r.create(ClientApi::class.java)
    @Provides fun provideFileApi(r: Retrofit): FileApi         = r.create(FileApi::class.java)
    @Provides fun provideLocationApi(r: Retrofit): LocationApi = r.create(LocationApi::class.java)
    @Provides fun provideSearchApi(r: Retrofit): SearchApi     = r.create(SearchApi::class.java)
    @Provides fun provideSpecialistApi(r: Retrofit): SpecialistApi = r.create(SpecialistApi::class.java)
    @Provides fun provideChatApi(r: Retrofit): ChatApi         = r.create(ChatApi::class.java)

    @Provides @Singleton
    fun provideChatDataStore(
        @Named("WsClient") wsClient: OkHttpClient,
        @Named("BASE_WS_URL") baseWs: String,
        gson: Gson,
        session: SessionDataStore
    ) = ChatDataStore(wsClient, baseWs, session, gson)

    @Provides @Named("BASE_WS_URL") fun baseWsUrl() = BASE_WS_URL
}
