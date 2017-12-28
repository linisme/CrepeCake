package net.idik.crepecake.sample.aspect;

import android.app.Activity;
import android.os.Bundle;

import net.idik.crepecake.annotations.InstanceOf;
import net.idik.crepecake.api.InvocationHandler;

/**
 * Created by linshuaibin on 2017/12/28.
 */


@InstanceOf(Activity.class)
public class ActivityAspect {

    void onCreate(InvocationHandler invocation, Bundle savedInstanceState) {

    }

}
