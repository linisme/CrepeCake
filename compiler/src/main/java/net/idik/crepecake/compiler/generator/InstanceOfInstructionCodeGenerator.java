package net.idik.crepecake.compiler.generator;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import net.idik.crepecake.compiler.data.InstanceOfSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

/**
 * Created by linshuaibin on 2017/12/28.
 */

public class InstanceOfInstructionCodeGenerator extends CodeGenerator<Set<InstanceOfSpec>> {

    private static final String PACKAGE_NAME = "net.idik.crepecake";
    private static final String CLASS_NAME = "InstanceOfInstruction";

    private InvocationHandlerCodeGenerator invocationHandlerCodeGenerator;

    public InstanceOfInstructionCodeGenerator(Messager messager, Filer filer) {
        super(filer, messager);
        invocationHandlerCodeGenerator = new InvocationHandlerCodeGenerator(filer, messager);
    }

    @Override
    public void generate(Set<InstanceOfSpec> datas) {

        int size = datas.size();

        TypeSpec.Builder instructionClassBuilder = TypeSpec.classBuilder(CLASS_NAME).addModifiers(Modifier.FINAL);

        instructionClassBuilder.addField(FieldSpec.builder(ArrayTypeName.of(String.class), "targets", Modifier.FINAL, Modifier.STATIC)
                .initializer("new String[$L]", size)
                .build());

        instructionClassBuilder.addField(FieldSpec.builder(ArrayTypeName.of(String.class), "processors", Modifier.FINAL, Modifier.STATIC)
                .initializer("new String[$L]", size)
                .build());
        CodeBlock.Builder staticCodeBlockBuilder = CodeBlock.builder();

        int i = 0;
        for (InstanceOfSpec spec : datas) {
            process(instructionClassBuilder, staticCodeBlockBuilder, spec, i++);
        }

        instructionClassBuilder.addStaticBlock(staticCodeBlockBuilder.build());

        JavaFile classFile = JavaFile.builder(PACKAGE_NAME, instructionClassBuilder.build()).build();

        try {
            classFile.writeTo(getFiler());
        } catch (FilerException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void process(TypeSpec.Builder instructionClassBuilder, CodeBlock.Builder staticCodeBlockBuilder, InstanceOfSpec spec, int index) {
        staticCodeBlockBuilder.add("targets[$L]=$S;\n", index, spec.getTargetClassName());
        staticCodeBlockBuilder.add("processors[$L]=$S;\n", index, spec.getProcessorClassName());
//        instructionClassBuilder.addField(FieldSpec.builder(String.class, "CREPE_" + spec.getTargetClassName().toUpperCase().replaceAll("\\.", "_"), Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC)
//                .initializer("$S", spec.getTargetClassName())
//                .build());

//        for (MethodSpec methodSpec : spec.getInvocationMethods()) {
//            invocationHandlerCodeGenerator.generate(methodSpec);
//        }

    }
}
