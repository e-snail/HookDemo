package bestpractice.hook.hookdemo.androidhook;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by wuyongbo on 16-5-27.
 */
public class HookInvocationHandler implements InvocationHandler {

    private static final String TAG = "HookInvocationHandler";

    private Object mBase;

    public HookInvocationHandler(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d(TAG, "invoke method:" + method.getName() + " called with args:" + Arrays.toString(args));

        return method.invoke(mBase, args);
    }
}