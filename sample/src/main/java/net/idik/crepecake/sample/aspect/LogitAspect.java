package net.idik.crepecake.sample.aspect;

import net.idik.crepecake.api.InvocationHandler;
import net.idik.crepecake.api.MethodInjector;

/**
 * Created by linshuaibin on 2018/1/2.
 */

public class LogitAspect implements MethodInjector {

    @Override
    public Object process(InvocationHandler invocationHandler, Object... args) {
        return invocationHandler.invoke(args);
    }

}
