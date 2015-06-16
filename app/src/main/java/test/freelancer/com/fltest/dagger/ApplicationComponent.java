package test.freelancer.com.fltest.dagger;


import javax.inject.Singleton;

import dagger.Component;
import test.freelancer.com.fltest.ListFragment;
import test.freelancer.com.fltest.MainActivity;

/**
 * Created by Lem on 6/16/2015.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity activity);

    void inject(ListFragment listFragment);
}
