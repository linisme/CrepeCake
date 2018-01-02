package net.idik.crepecake.compiler;

import com.google.auto.service.AutoService;

import net.idik.crepecake.annotations.InstanceOf;
import net.idik.crepecake.compiler.data.AnnotationSpec;
import net.idik.crepecake.compiler.data.InstanceOfSpec;
import net.idik.crepecake.compiler.generator.InstanceOfInstructionCodeGenerator;
import net.idik.crepecake.compiler.parser.InstanceOfParser;

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

    private InstanceOfParser instanceOfParser;


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
        instanceOfParser = new InstanceOfParser(typeUtils, elementUtils, messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<AnnotationSpec> datas = new HashSet<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(InstanceOf.class)) {

            if (!Validator.valid(element, InstanceOf.class, messager)) {
                continue;
            }

            InstanceOfSpec data = instanceOfParser.parse((TypeElement) element);

            if (data != null) {
                datas.add(data);
            }

        }

        new InstanceOfInstructionCodeGenerator(messager, filer).generate(datas);

        return true;
    }
}
