package test.arctouch.tmdbapp;

import android.app.Application;

import test.arctouch.tmdbapp.di.AppComponent;
import test.arctouch.tmdbapp.di.DaggerAppComponent;
import test.arctouch.tmdbapp.di.NetworkModule;


public class TMDdApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        resolveDependency();
        super.onCreate();
    }

    private void resolveDependency() {
        appComponent = DaggerAppComponent.builder()
                .networkModule(new NetworkModule(getBaseURL(), getApiKey()))
                .build();
    }

    public static AppComponent getApiComponent() {
        return appComponent;
    }

    private String getBaseURL() {
        return getString(R.string.api_base_url);
    }

    private String getApiKey() {
        return getString(R.string.api_key);
    }
}
