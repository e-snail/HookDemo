package bestpractice.hook.hookdemo.androidhook;

import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wuyongbo on 16-5-27.
 */
public class HookHelper {

    /**
     * 获得{@link android.app.ActivityManagerNative}类的{@link gDefault}成员变量
     */
    public static void hookActivityManager() {
        try {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

            // 获取 gDefault 这个字段, 想办法替换它
            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);

            Object gDefault = gDefaultField.get(null);

            // 4.x以上的gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的字段
            Class<?> singleton = Class.forName("android.util.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            // ActivityManagerNative 的gDefault对象里面原始的 IActivityManager对象
            Object rawIActivityManager = mInstanceField.get(gDefault);

            /**
             * 创建{@link android.app.IActivityManager}一个这个对象的代理对象, 让代理对象替换gDefault
             */
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { iActivityManagerInterface }, new HookInvocationHandler(rawIActivityManager));
            mInstanceField.set(gDefault, proxy);

        } catch (Exception e) {
            throw new RuntimeException("Hook Failed", e);
        }

    }

    /**
     * 替换{@link android.app.ActivityThread}里的{@link sPackageManager}为代理对象
     * 替换{@link android.app.ApplicationPackageManager}里的mPm对象为代理对象
     * 替换{@link android.app.ActivityThread}里的{@link mInstrumentation}为代理对象
     */
    public static void hookPackageManager(Context context) {
        try {
            // 获取全局的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取ActivityThread里面原始的 sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);

            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
                    new Class<?>[] { iPackageManagerInterface },
                    new HookInvocationHandler(sPackageManager));

            // 1. 替换掉ActivityThread里面的 sPackageManager 字段
            sPackageManagerField.set(currentActivityThread, proxy);

            // 2. 替换 ApplicationPackageManager里面的 mPm对象
            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);

            // 3. 替换掉ActivityThread里面的mInstrumentation成员变量
            Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

            HookInstrumentation hookInstrumentation = new HookInstrumentation(mInstrumentation);
            mInstrumentationField.set(currentActivityThread, hookInstrumentation);

        } catch (Exception e) {
            throw new RuntimeException("hook failed", e);
        }
    }
}
