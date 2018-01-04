package net.idik.crepecake.sample.aspect;

import android.view.View;

import net.idik.crepecake.annotations.Aspect;
import net.idik.crepecake.api.InvocationHandler;

/**
 * Created by linshuaibin on 2018/1/2.
 */

@Aspect(View.OnClickListener.class)
public class OnClickListenerAspect {

    public void onClick(InvocationHandler invocationHandler, View view) {
        System.out.println("---------------------" + view);
        invocationHandler.invoke(view);
        System.out.println("-000----------------------");
    }

}
