package bestpractice.hook.hookdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Proxy;

import bestpractice.hook.hookdemo.dynamicproxy.AccessInternet;
import bestpractice.hook.hookdemo.dynamicproxy.AccessProxyHandler;
import bestpractice.hook.hookdemo.dynamicproxy.IAccess;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
