package com.hmelizarraraz.cleanarchitecture.root;

import com.hmelizarraraz.cleanarchitecture.http.TwitchModule;
import com.hmelizarraraz.cleanarchitecture.login.LoginActivity;
import com.hmelizarraraz.cleanarchitecture.login.LoginModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, LoginModule.class, TwitchModule.class})
public interface ApplicationComponent {

    void inject(LoginActivity target);

}
