package net.idik.crepecake.compiler.generator;

import net.idik.crepecake.compiler.data.MethodSpec;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;

/**
 * Created by linshuaibin on 2017/12/29.
 */

public class InvocationHandlerCodeGenerator extends CodeGenerator<MethodSpec> {

    public InvocationHandlerCodeGenerator(Filer filer, Messager messager) {
        super(filer, messager);
    }

    @Override
    public void generate(MethodSpec spec) {

    }

}
