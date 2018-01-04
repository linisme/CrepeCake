package net.idik.crepecake.compiler;


import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by linshuaibin on 2017/12/29.
 */
public class Utils {

    public static String getRawString(Annotation annotation, String key) {
        String source = annotation.toString();
        int start = source.indexOf('(');
        int end = source.length() - 1;
        source = source.substring(start + 1, end);
        String[] tokens = source.split(",");
        String className = null;
        for (String token : tokens) {
            token = token.trim();
            String[] parts = token.split("=");
            if (parts.length == 2 && key.equals(parts[0])) {
                className = parts[1];
                break;
            }
        }
        return className;
    }

    public static TypeElement getTypeElementByClassName(Elements elements, String className) {
        return elements.getTypeElement(className);
    }

}
