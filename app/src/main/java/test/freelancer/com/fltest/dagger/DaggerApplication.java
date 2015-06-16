package test.freelancer.com.fltest.dagger;

import com.orm.SugarApp;

/**
 * Created by Lem on 6/16/2015.
 */
public class DaggerApplication extends SugarApp {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
