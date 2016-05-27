package bestpractice.hook.hookdemo.androidhook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by wuyongbo on 16-5-27.
 */
public class HookActivity extends Activity {

    //这个方法比onCreate调用早; 在这里Hook比较好.
    @Override
    protected void attachBaseContext(Context newBase) {
        HookHelper.hookActivityManager();
        HookHelper.hookPackageManager(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO
    }
}
