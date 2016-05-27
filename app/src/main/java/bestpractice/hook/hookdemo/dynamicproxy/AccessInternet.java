package bestpractice.hook.hookdemo.dynamicproxy;

import android.util.Log;

/**
 * Created by wuyongbo on 16-5-27.
 */
public class AccessInternet implements IAccess {

    final static String TAG = AccessInternet.class.getSimpleName();

    @Override
    public void onWork() {
        Log.d(TAG, "AccessInternet::onWork");
    }
}
