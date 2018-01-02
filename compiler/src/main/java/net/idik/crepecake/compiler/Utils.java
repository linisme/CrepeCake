package net.idik.crepecake.compiler;


import java.lang.annotation.Annotation;

/**
 * Created by linshuaibin on 2017/12/29.
 */
public class Utils {

    public static String getRawString(Annotation annotation, String key) {
        String source = annotation.toString();
        int start = source.indexOf('(');
        int end = source.length() - 1;
        source = source.substring(start, end);
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
}
