package test.arctouch.tmdbapp.di;

import javax.inject.Singleton;

import dagger.Component;
import test.arctouch.tmdbapp.ui.activities.main.MainActivity;
import test.arctouch.tmdbapp.ui.activities.result.SearchResultActivity;


@Singleton
@Component(modules = {NetworkModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(SearchResultActivity searchResultActivity);
}
