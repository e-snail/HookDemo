package bestpractice.hook.hookdemo;

import android.app.Application;
import android.content.Context;

import bestpractice.hook.hookdemo.activityhook.CustomInstrumentation;

/**
 * Created by wuyongbo on 16-8-15.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CustomInstrumentation.hookInstrumentation();
    }
}
