package net.idik.crepecake.compiler.data;

import java.util.List;

/**
 * Created by linshuaibin on 2017/12/28.
 */

public class InstanceOfSpec extends AnnotationSpec {

    private String className;

    private List<MethodSpec> invocationMethods;

    public InstanceOfSpec(String className, List<MethodSpec> invocationMethods) {
        this.className = className;
        this.invocationMethods = invocationMethods;
    }

    public String getClassName() {
        return className;
    }

    public List<MethodSpec> getInvocationMethods() {
        return invocationMethods;
    }
}
