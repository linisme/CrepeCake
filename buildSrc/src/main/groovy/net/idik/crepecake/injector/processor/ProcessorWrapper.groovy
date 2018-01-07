package net.idik.crepecake.injector.processor

import javassist.CtClass
import javassist.CtMethod

class ProcessorWrapper {

    private HashMap<CtMethod, Integer> modifiersCache

    ProcessorWrapper() {
        this.modifiersCache = new HashMap<>()
    }

    public int getModifiers(CtMethod method) {
        Integer result = modifiersCache.get(method)
        if (result == null) {
            result = method.getModifiers()
            modifiersCache.put(method, result)
        }
        return result
    }

}