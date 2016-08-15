package bestpractice.hook.hookdemo.activityhook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wuyongbo on 16-8-15.
 */
public class CustomInstrumentation extends Instrumentation {

    Instrumentation mBase;

    public CustomInstrumentation(Instrumentation base) {
        mBase = base;
    }

    @Override
    public Activity newActivity(ClassLoader classLoader, String className, Intent intent)
        throws IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        Log.d("CustomInstrumentation", "This activity is hooked!");

        Activity activity = null;

        return super.newActivity(classLoader, className, intent);
    }

    public static void hookInstrumentation() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThread.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThreadObj = currentActivityThreadMethod.invoke(null);   /** 因为currentActivityThread是静态方法 */

            Field mInstrumentation = activityThread.getDeclaredField("mInstrumentation");
            mInstrumentation.setAccessible(true);

            Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(currentActivityThreadObj);
            CustomInstrumentation customInstrumentation = new CustomInstrumentation(instrumentation);

            mInstrumentation.set(currentActivityThreadObj, customInstrumentation);

            Log.d("CustomInstrumentation", "hook instrumentation success!!!");
        } catch (ClassNotFoundException e) {
            Log.e("CustomInstrumentation", "hook instrumentation failed!!! with exception\n" + e.toString());
        } catch (NoSuchMethodException e) {
            Log.e("CustomInstrumentation", "hook instrumentation failed!!! with exception\n" + e.toString());
        } catch (InvocationTargetException e) {
            Log.e("CustomInstrumentation", "hook instrumentation failed!!! with exception\n" + e.toString());
        } catch (IllegalAccessException e) {
            Log.e("CustomInstrumentation", "hook instrumentation failed!!! with exception\n" + e.toString());
        } catch (NoSuchFieldException e) {
            Log.e("CustomInstrumentation", "hook instrumentation failed!!! with exception\n" + e.toString());
        }
    }
}
