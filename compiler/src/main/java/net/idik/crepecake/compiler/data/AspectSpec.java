package net.idik.crepecake.compiler.data;

import java.util.List;

/**
 * Created by linshuaibin on 2017/12/28.
 */

public class AspectSpec {

    private String configClassName;

    private String processorClassName;

    private List<MethodSpec> invocationMethods;

    public AspectSpec(String configClassName, String processorClassName, List<MethodSpec> invocationMethods) {
        this.configClassName = configClassName;
        this.invocationMethods = invocationMethods;
        this.processorClassName = processorClassName;
    }

    public String getConfigClassName() {
        return configClassName;
    }

    public String getProcessorClassName() {
        return processorClassName;
    }

    public List<MethodSpec> getInvocationMethods() {
        return invocationMethods;
    }
}
