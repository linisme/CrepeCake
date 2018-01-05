package net.idik.crepecake.sample.aspect;

import android.view.View;

import net.idik.crepecake.api.AspectConfig;
import net.idik.crepecake.sample.MainActivity;

/**
 * Created by linshuaibin on 2018/1/4.
 */

public class OnClickAspectConfig extends AspectConfig {
    @Override
    protected boolean isEnable() {
        return super.isEnable();
    }

    @Override
    public boolean isHook(Class clazz) {
        return View.OnLongClickListener.class.isAssignableFrom(clazz) || View.OnClickListener.class.isAssignableFrom(clazz);
    }
}
