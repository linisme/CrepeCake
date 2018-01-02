package net.idik.crepecake.api;

/**
 * Created by linshuaibin on 2018/1/2.
 */

public interface MethodInjector {
    Object process(InvocationHandler invocationHandler, Object... args);
}
