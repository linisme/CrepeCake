package net.idik.crepecake.compiler.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import net.idik.crepecake.compiler.data.AnnotationSpec;
import net.idik.crepecake.compiler.data.InstanceOfSpec;
import net.idik.crepecake.compiler.data.MethodSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

/**
 * Created by linshuaibin on 2017/12/28.
 */

public class InstanceOfInstructionCodeGenerator extends CodeGenerator<Set<AnnotationSpec>> {

    private static final String PACKAGE_NAME = "net.idik.crepecake";
    private static final String CLASS_NAME = "InstanceOfInstruction";

    private InvocationHandlerCodeGenerator invocationHandlerCodeGenerator;

    public InstanceOfInstructionCodeGenerator(Messager messager, Filer filer) {
        super(filer, messager);
        invocationHandlerCodeGenerator = new InvocationHandlerCodeGenerator(filer, messager);
    }

    @Override
    public void generate(Set<AnnotationSpec> datas) {

        TypeSpec.Builder instructionClassBuilder = TypeSpec.classBuilder(CLASS_NAME).addModifiers(Modifier.FINAL);

        for (AnnotationSpec data : datas) {
            if (data instanceof InstanceOfSpec) {
                process(instructionClassBuilder, (InstanceOfSpec) data);
            }
        }

        JavaFile classFile = JavaFile.builder(PACKAGE_NAME, instructionClassBuilder.build()).build();

        try {
            classFile.writeTo(getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void process(TypeSpec.Builder instructionClassBuilder, InstanceOfSpec data) {
        instructionClassBuilder.addField(FieldSpec.builder(String.class, "CREPE_" + data.getClassName().toUpperCase().replaceAll("\\.", "_"), Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
                .initializer("$S", data.getClassName())
                .build());

        for (MethodSpec spec : data.getInvocationMethods()) {
            invocationHandlerCodeGenerator.generate(spec);
        }

    }
}
