package bestpractice.hook.hookdemo.dynamicproxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by wuyongbo on 16-5-27.
 */
public class AccessProxyHandler implements InvocationHandler {

    final static String TAG = AccessInternet.class.getSimpleName();

    private Object principal;

    public AccessProxyHandler(Object principal) {
        this.principal = principal;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d(TAG, "--------before invoke");
        Object ob = method.invoke(principal, args);
        Log.d(TAG, "--------after invoke");

        return ob;
    }
}
