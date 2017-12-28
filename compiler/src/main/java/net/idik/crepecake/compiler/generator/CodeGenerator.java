package net.idik.crepecake.compiler.generator;

import net.idik.crepecake.compiler.data.AnnotationData;
import net.idik.crepecake.compiler.data.InstanceOfData;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;

/**
 * Created by linshuaibin on 2017/12/28.
 */

public class CodeGenerator {

    public static void generate(Set<AnnotationData> datas, Messager messager, Filer filer) {

        for (AnnotationData data : datas) {
            if (data instanceof InstanceOfData) {
                generate((InstanceOfData) data, messager, filer);
            }
        }

    }

    private static void generate(InstanceOfData data, Messager messager, Filer filer) {
    }
}
