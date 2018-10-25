package com.hmelizarraraz.cleanarchitecture.http;

import com.hmelizarraraz.cleanarchitecture.http.twitch.Twitch;
import com.hmelizarraraz.cleanarchitecture.http.twitch.TwitchStream;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TwitchAPI {

    /**
     * Metodo para obtener los juegos mas populares
     * @param clientId Es el id del cliente de twitch
     * @return Objeto con lista de juegos
     */
    @GET("games/top")
    Call<Twitch> getTopGames(@Header("Client-ID") String clientId);

    @GET("games/top")
    Observable<Twitch> getTopGamesObservable(@Header("Client-ID") String clientId);

    /**
     * Metodo para obtener los streams
     * @param clientId Id de cliente de Twitch
     * @return Observable
     */
    @GET("streams")
    Observable<TwitchStream> getStreamGamesObservable(@Header("Client-ID") String clientId);

    @GET("games")
    Observable<Twitch> getGameById(@Header("Client-ID") String clientId, @Query("id") String idGame);
}
