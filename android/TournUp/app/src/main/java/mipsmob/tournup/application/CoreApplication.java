package mipsmob.tournup.application;

import android.app.Application;

import com.parse.Parse;

public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "22d6Ninlt3Gcjv4fXMljncmHsboYlnCx5KkMIWt8", "2udREdDAjBRKrOcAEQAEJWvqBYDaDDVzjmPHOlbA");
    }

}
