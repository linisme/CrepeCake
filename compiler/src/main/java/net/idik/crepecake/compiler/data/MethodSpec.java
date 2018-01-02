package net.idik.crepecake.compiler.data;

/**
 * Created by linshuaibin on 2017/12/29.
 */

public class MethodSpec {

    private String name;

    private String returnType;

    private VariantSpec[] parameters;

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public VariantSpec[] getParameters() {
        return parameters;
    }

    public MethodSpec(String name, String returnType, VariantSpec[] parameters) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }
}
