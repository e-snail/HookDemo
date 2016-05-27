package bestpractice.hook.hookdemo.androidhook;

import android.os.Bundle;

import bestpractice.hook.hookdemo.R;

/**
 * Created by wuyongbo on 16-5-27.
 */
public class PluginActivity extends HookActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
    }
}
