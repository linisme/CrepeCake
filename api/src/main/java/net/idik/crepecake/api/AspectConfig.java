package net.idik.crepecake.api;

/**
 * Created by linshuaibin on 2018/1/4.
 */

public abstract class AspectConfig {
    protected boolean isEnable() {
        return true;
    }

    public boolean test(Class clazz) {
        return isEnable() && isHook(clazz);
    }

    protected abstract boolean isHook(Class clazz);
}
