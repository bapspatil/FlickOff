package bapspatil.flickoff;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by bapspatil
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
