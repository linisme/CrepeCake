package net.idik.crepecake.sample.aspect;

import net.idik.crepecake.api.InvocationHandler;

/**
 * Created by linshuaibin on 2018/1/2.
 */

public class K extends InvocationHandler {
    public K(Object caller) {
        super(caller);
    }

    @Override
    protected Object call(Object[] args) {
        return null;
    }
}
