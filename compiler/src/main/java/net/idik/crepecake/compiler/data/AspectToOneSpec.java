package net.idik.crepecake.compiler.data;

import java.util.List;

/**
 * Created by linshuaibin on 2018/1/4.
 */

public class AspectToOneSpec extends AspectSpec {
    private final static String ASPECT_TO_ONE_PACKAGE = "net.idik.crepecake.configs";

    private String targetClassName;

    public AspectToOneSpec(String targetClassName, String processorClassName, List<MethodSpec> invocationMethods) {
        super(getInstanceClassName(targetClassName, processorClassName), processorClassName, invocationMethods);
        this.targetClassName = targetClassName;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    private static String getInstanceClassName(String targetClassName, String processorClassName) {
        return ASPECT_TO_ONE_PACKAGE + "." + targetClassName.replaceAll("\\.", "_") + "_" + processorClassName.replaceAll("\\.", "_") + "_AspectConfig";
    }
}
