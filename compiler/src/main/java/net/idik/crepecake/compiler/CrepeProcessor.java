package net.idik.crepecake.compiler;

import com.google.auto.service.AutoService;

import net.idik.crepecake.compiler.data.AnnotationData;
import net.idik.crepecake.compiler.data.InstanceOfData;
import net.idik.crepecake.compiler.generator.CodeGenerator;
import net.idik.crepecake.compiler.parser.InstanceOfParser;
import net.idik.crepecake.annotations.InstanceOf;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Process.class)
public class CrepeProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Messager messager;
    private Filer filer;
    private Elements elementUtils;


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(InstanceOf.class.getCanonicalName());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<AnnotationData> datas = new HashSet<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(InstanceOf.class)) {

            if (!Validator.valid(element, InstanceOf.class, messager)) {
                continue;
            }

            InstanceOfData data = InstanceOfParser.parse(element, typeUtils, elementUtils, messager);

            if (data != null) {
                datas.add(data);
            }

        }

        CodeGenerator.generate(datas, messager, filer);

        return true;
    }
}
