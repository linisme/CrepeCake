package net.idik.crepecake.compiler;

import com.google.auto.service.AutoService;

import net.idik.crepecake.annotations.Aspect;
import net.idik.crepecake.compiler.data.AspectSpec;
import net.idik.crepecake.compiler.generator.ApsectInstructionCodeGenerator;
import net.idik.crepecake.compiler.parser.AspectParser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class CrepeProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Messager messager;
    private Filer filer;
    private Elements elementUtils;

    private AspectParser aspectParser;


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Aspect.class.getCanonicalName());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        aspectParser = new AspectParser(typeUtils, elementUtils, messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<AspectSpec> datas = new HashSet<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(Aspect.class)) {

            if (!Validator.valid(element, Aspect.class, messager)) {
                continue;
            }

            AspectSpec data = aspectParser.parse((TypeElement) element);

            if (data != null) {
                datas.add(data);
            }

        }

        new ApsectInstructionCodeGenerator(messager, filer).generate(datas);

        return true;
    }
}
