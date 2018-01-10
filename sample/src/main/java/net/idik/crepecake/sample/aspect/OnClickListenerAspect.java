package net.idik.crepecake.sample.aspect;

import android.view.View;

import net.idik.crepecake.annotation.Aspect;
import net.idik.crepecake.api.InvocationHandler;

/**
 * Created by linshuaibin on 2018/1/2.
 */

//@Aspect(OnClickAspectConfig.class)
public class OnClickListenerAspect {

    public void onClick(InvocationHandler invocationHandler, View view) {
        System.out.println("OnClick: " + view);
        invocationHandler.invoke(view);
    }

    public boolean onLongClick(InvocationHandler invocationHandler, View view) {
        boolean isConsume = (boolean) invocationHandler.invoke(view);
        if (isConsume) {
            System.out.println("OnLongClick: " + view);
        }
        return isConsume;
    }

}
