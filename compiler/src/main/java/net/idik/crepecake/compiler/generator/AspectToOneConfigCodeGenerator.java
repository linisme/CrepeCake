package net.idik.crepecake.compiler.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.idik.crepecake.api.AspectConfig;
import net.idik.crepecake.compiler.data.AspectToOneSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;

/**
 * Created by linshuaibin on 2018/1/4.
 */

public class AspectToOneConfigCodeGenerator extends CodeGenerator<AspectToOneSpec> {
    AspectToOneConfigCodeGenerator(Filer filer, Messager messager) {
        super(filer, messager);
    }

    @Override
    public void generate(AspectToOneSpec spec) {

        String className = spec.getConfigClassName();

        int spiteIndex = className.lastIndexOf('.');
        String simpleName = className.substring(spiteIndex + 1);

        String packageName = className.substring(0, spiteIndex);

        TypeSpec.Builder configClassBuilder = TypeSpec.classBuilder(simpleName).addModifiers(Modifier.FINAL).superclass(AspectConfig.class);

        configClassBuilder.addMethod(MethodSpec.methodBuilder("isHook")
                .addParameter(Class.class, "clazz")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .returns(TypeName.BOOLEAN)
//                .addStatement("return $L.class.isAssignableFrom(clazz)", spec.getTargetClassName())
                .addStatement("return $L.class.equals(clazz)", spec.getTargetClassName())
                .build()
        );

//        @Override
//        protected boolean isHook(Class clazz) {
//            return false;
//        }

        JavaFile classFile = JavaFile.builder(packageName, configClassBuilder.build()).build();

        try {
            classFile.writeTo(getFiler());
        } catch (FilerException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
