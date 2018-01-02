package net.idik.crepecake.compiler.data;

/**
 * Created by linshuaibin on 2017/12/29.
 */

public class VariantSpec {

    private String name;

    private String typeName;

    public VariantSpec(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }
}
