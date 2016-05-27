package bestpractice.hook.hookdemo.androidhook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by wuyongbo on 16-5-27.
 */
public class HookInstrumentation extends Instrumentation {
    private static final String TAG = HookInstrumentation.class.getSimpleName();

    // ActivityThread中原始的对象, 保存起来
    Instrumentation mBase;

    public HookInstrumentation(Instrumentation mBase) {
        this.mBase = mBase;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {

        Log.d(TAG, "\nstartActivity,\nwith params: \n" + "who = [" + who + "], " +
                "\ncontextThread = [" + contextThread + "], \ntoken = [" + token + "], " +
                "\ntarget = [" + target + "], \nintent = [" + intent +
                "], \nrequestCode = [" + requestCode + "], \noptions = [" + options + "]");

        // 开始调用原始的方法, 调不调用随你,但是不调用的话, 所有的startActivity都失效了.
        // 由于这个方法是隐藏的,因此需要使用反射调用;首先找到这个方法
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class,
                    Intent.class, int.class, Bundle.class);
            execStartActivity.setAccessible(true);

            return (ActivityResult) execStartActivity.invoke(mBase, who, contextThread, token, target, intent, requestCode, options);

        } catch (Exception e) {
            throw new RuntimeException("do not support!!! pls adapt it");
        }
    }
}