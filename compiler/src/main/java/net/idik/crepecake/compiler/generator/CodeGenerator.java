package net.idik.crepecake.compiler.generator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;

/**
 * Created by linshuaibin on 2017/12/29.
 */

public abstract class CodeGenerator<T> {
    private Messager messager;
    private Filer filer;

    CodeGenerator(Filer filer, Messager messager) {
        this.filer = filer;
        this.messager = messager;
    }

    public abstract void generate(T spec);

    public Filer getFiler() {
        return filer;
    }

    public Messager getMessager() {
        return messager;
    }
}
