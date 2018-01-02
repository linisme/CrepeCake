package net.idik.crepecake.compiler.data;

import java.util.List;

/**
 * Created by linshuaibin on 2017/12/28.
 */

public class InstanceOfSpec extends AnnotationSpec {

    private String targetClassName;

    private String processorClassName;

    private List<MethodSpec> invocationMethods;

    public InstanceOfSpec(String targetClassName, String processorClassName, List<MethodSpec> invocationMethods) {
        this.targetClassName = targetClassName;
        this.invocationMethods = invocationMethods;
        this.processorClassName = processorClassName;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public String getProcessorClassName() {
        return processorClassName;
    }

    public List<MethodSpec> getInvocationMethods() {
        return invocationMethods;
    }
}
