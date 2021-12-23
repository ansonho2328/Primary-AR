package hk.edu.ouhk.arprimary.manager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    public SQLiteManager getSQLiteManager(){
        return new SQLiteManager();
    }




}
