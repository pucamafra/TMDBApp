package test.arctouch.tmdbapp.di;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.squareup.okhttp.OkHttpClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import test.arctouch.tmdbapp.config.Constants;
import test.arctouch.tmdbapp.data.TMDbDataSource;
import test.arctouch.tmdbapp.data.TMDbRepository;
import test.arctouch.tmdbapp.data.remote.TMDbRemoteData;
import test.arctouch.tmdbapp.network.TMDbApi;

@Module
public class NetworkModule {
    private final int TIME_OUT = 10;
    private final TimeUnit timeUnit = TimeUnit.SECONDS;
    private String baseURL;
    private String apiKey;

    public NetworkModule(String baseURL, String apiKey) {
        this.baseURL = baseURL;
        this.apiKey = apiKey;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, (JsonDeserializer) (json, typeOfT, context) -> {
            String dateString = json.getAsString();

            if (dateString.isEmpty()) {
                return null;
            }

            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                return df.parse(json.getAsString());
            } catch (final ParseException e) {
                return null;
            }
        });


        return builder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(TIME_OUT, timeUnit);
        okHttpClient.setConnectTimeout(TIME_OUT, timeUnit);
        okHttpClient.setWriteTimeout(TIME_OUT, timeUnit);
        return okHttpClient;
    }

    @Provides
    @Singleton
    RestAdapter.Builder provideRestAdapter(Gson gson, OkHttpClient okHttpClient) {
        return new RestAdapter.Builder()
                .setEndpoint(this.baseURL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("server:message"))
                .setClient(new OkClient(okHttpClient));
    }

    @Provides
    @Singleton
    TMDbDataSource provideTMDbDataSource(TMDbRemoteData tmDbRemoteData) {
        return new TMDbRepository(tmDbRemoteData);
    }


    @Provides
    @Singleton
    TMDbApi provideMarvelApi(RestAdapter.Builder restAdapterBuilder) {
        restAdapterBuilder.setRequestInterceptor(request -> request.addQueryParam(Constants.API_KEY, this.apiKey));
        return restAdapterBuilder.build().create(TMDbApi.class);
    }
}
