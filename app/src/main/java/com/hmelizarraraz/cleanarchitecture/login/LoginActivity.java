package com.hmelizarraraz.cleanarchitecture.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hmelizarraraz.cleanarchitecture.R;
import com.hmelizarraraz.cleanarchitecture.http.TwitchAPI;
import com.hmelizarraraz.cleanarchitecture.http.twitch.Game;
import com.hmelizarraraz.cleanarchitecture.http.twitch.Stream;
import com.hmelizarraraz.cleanarchitecture.http.twitch.Twitch;
import com.hmelizarraraz.cleanarchitecture.http.twitch.TwitchStream;
import com.hmelizarraraz.cleanarchitecture.root.App;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements LoginActivityMVP.View {

    @Inject
    LoginActivityMVP.Presenter presenter;

    @Inject
    TwitchAPI twitchAPI;

    @BindView(R.id.edit_text_first_name)
    EditText firstName;

    @BindView(R.id.edit_text_last_name)
    EditText lastName;

    @BindView(R.id.button_login)
    Button loginButton;

    @BindString(R.string.first_name)
    String firstNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginButtonClicked();
            }
        });

        Toast.makeText(this, firstNameText, Toast.LENGTH_SHORT).show();

        // Ejemplo de uso de la api de Twitch con retrofit
        /*Call<Twitch> call = twitchAPI.getTopGames("b2wz743648p6j0u38nxbxyvcc1yyop");

        call.enqueue(new Callback<Twitch>() {
            @Override
            public void onResponse(Call<Twitch> call, Response<Twitch> response) {
                List<Game> topGames = response.body().getGame();

                for (Game game : topGames) {
                    System.out.println(game.getName());
                }

            }

            @Override
            public void onFailure(Call<Twitch> call, Throwable t) {
                t.printStackTrace();
            }
        });*/

        // Ejemplo de uso de la api con llamada reactiva
        //getGames();

        twitchAPI
                .getStreamGamesObservable("b2wz743648p6j0u38nxbxyvcc1yyop")
                .flatMap(new Function<TwitchStream, Observable<Stream>>() {
                    @Override
                    public Observable<Stream> apply(TwitchStream twitchStream) throws Exception {
                        return Observable.fromIterable(twitchStream.getStream());
                    }
                })
                .filter(new Predicate<Stream>() {
                    @Override
                    public boolean test(Stream stream) {
                        return !stream.getGameId().isEmpty() &&
                                stream.getViewerCount() >= 10000 &&
                                stream.getLanguage().equals("en");
                    }
                })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Stream>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Stream stream) {
                        getGame(stream.getGameId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getGame(String gameId) {
        twitchAPI.getGameById("b2wz743648p6j0u38nxbxyvcc1yyop", gameId)
                .flatMap(new Function<Twitch, Observable<Game>>() {
                    @Override
                    public Observable<Game> apply(Twitch twitch) {
                        return Observable.just(twitch.getGame().get(0));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Game>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Game game) {
                        System.out.println("Game name= " + game.getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void getGames() {
        twitchAPI.getTopGamesObservable("b2wz743648p6j0u38nxbxyvcc1yyop")
                .flatMap(new Function<Twitch, Observable<Game>>() {
                    @Override
                    public Observable<Game> apply(Twitch twitch) {
                        return Observable.fromIterable(twitch.getGame());
                    }
                })
                .flatMap(new Function<Game, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Game game) {
                        return Observable.just(game.getName());
                    }
                })
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return s.contains("w") || s.contains("W");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String name) {
                        System.out.println("RxJava says: " + name);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getCurrentUser();
    }

    @Override
    public String getFirstName() {
        return this.firstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return this.lastName.getText().toString();
    }

    @Override
    public void showUserNotAvailable() {
        Toast.makeText(this, "Error, el usuario no esta disponible", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInputError() {
        Toast.makeText(this, "Error, el nombre ni el apellido deben estar vacios", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserSaved() {
        Toast.makeText(this, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName.setText(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName.setText(lastName);
    }
}
