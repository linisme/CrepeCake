package net.idik.crepecake.sample.aspect;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import net.idik.crepecake.annotations.InstanceOf;
import net.idik.crepecake.api.InvocationHandler;
import net.idik.crepecake.sample.MainActivity;

/**
 * Created by linshuaibin on 2017/12/28.
 */


@InstanceOf(MainActivity.class)
public class ActivityAspect {

    protected void onCreate(InvocationHandler invocationHandler, Bundle savedInstanceState) {
        System.out.println("onCreate------------------------");
        invocationHandler.invoke(savedInstanceState);
        System.out.println("onCreate------------------------end");
    }

    protected void onStart(InvocationHandler invocationHandler) {
        System.out.println("onStart-------------------------");
        invocationHandler.invoke();
    }

    protected void onStop(InvocationHandler invocationHandler) {
        System.out.println("onStop-------------------------");
        invocationHandler.invoke();
    }

    public android.support.v7.app.ActionBar getSupportActionBar(InvocationHandler invocationHandler) {
        ActionBar bar = (ActionBar) invocationHandler.invoke();
        System.out.println("getActionBar---------------------------" + bar.toString());
        return bar;
    }
}
