package net.idik.crepecake.compiler;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;

/**
 * Created by linshuaibin on 2017/12/28.
 */

public class Validator {
    public static boolean valid(Element annotatedElement, Class annotation, Messager messager) {
        return true;
    }
}
