package bestpractice.hook.hookdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Proxy;

import bestpractice.hook.hookdemo.androidhook.HookActivity;
import bestpractice.hook.hookdemo.androidhook.PluginActivity;
import bestpractice.hook.hookdemo.dynamicproxy.AccessInternet;
import bestpractice.hook.hookdemo.dynamicproxy.AccessProxyHandler;
import bestpractice.hook.hookdemo.dynamicproxy.IAccess;

public class MainActivity extends HookActivity {

    final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Context context = getBaseContext();
        TextView textView = (TextView)findViewById(R.id.helloworld);
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onCreate onClick");
                    Intent intent = new Intent(context, PluginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        /**Java的动态代理模式*/

        /**被代理的对象*/
        AccessInternet accessInternet = new AccessInternet();
        /**通过Proxy和InvocationHandler生成代理对象*/
        IAccess agent = (IAccess) Proxy.newProxyInstance(
                AccessInternet.class.getClassLoader(),      /**被代理对象的ClassLoader*/
                new Class[] {IAccess.class},                /**被代理对象的接口*/
                new AccessProxyHandler(accessInternet));    /**InvocationHandler对象*/
        /**调用代理对象*/
        agent.onWork();
    }
}
