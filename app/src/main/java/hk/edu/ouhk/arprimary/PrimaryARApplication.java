package hk.edu.ouhk.arprimary;

import android.app.Application;

import hk.edu.ouhk.arprimary.manager.ApplicationComponent;
import hk.edu.ouhk.arprimary.manager.DaggerApplicationComponent;

public class PrimaryARApplication extends Application {

    ApplicationComponent appComponent = DaggerApplicationComponent.create();

}
