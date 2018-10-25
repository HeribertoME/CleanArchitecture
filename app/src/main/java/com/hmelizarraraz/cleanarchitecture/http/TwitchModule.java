package com.hmelizarraraz.cleanarchitecture.http;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TwitchModule {

    public final String BASE_URL = "https://api.twitch.tv/helix/";

//    @Provides
//    public OkHttpClient provideHttpClient() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
//    }

    @Provides
    public OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides
    public Retrofit provideRetrofit(String baseUrl, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // llamadas reactive rxjava
                .build();
    }

    @Provides
    public TwitchAPI provideTwitchService() {
        return provideRetrofit(BASE_URL, provideHttpClient()).create(TwitchAPI.class);
    }

}
