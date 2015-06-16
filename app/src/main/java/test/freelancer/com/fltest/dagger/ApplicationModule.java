package test.freelancer.com.fltest.dagger;

import android.app.Application;
import android.content.Context;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import test.freelancer.com.fltest.rest.TvgService;

/**
 * Created by Lem on 6/16/2015.
 */
@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    TvgService provideTvgService() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(TvgService.ENDPOINT)
                .build();

        return adapter.create(TvgService.class);
    }

}
