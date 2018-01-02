package net.idik.crepecake.injector

import javassist.ClassPool
import javassist.CtClass

class InstanceOfInjector {

    private static ClassPool pool = ClassPool.getDefault()

    private static Set<CtClass> targets = new HashSet<>()
    public static final String INSTRUCTION = "net.idik.crepecake.InstanceOfInstruction"

    InstanceOfInjector() {
        pool.getCtClass(INSTRUCTION).getDeclaredFields().each {
            targets.add(pool.getCtClass(it.constantValue))
        }
    }

    void inject(String path) {
        File dir = new File(path)
        dir.eachFileRecurse { file ->
            String filePath = file.absolutePath
            if (file.exists() && filePath.endsWith(".class")) {
                def className = getClassName(path, filePath)
                if (!className.startsWith("android.")) {
                    def ct = pool.getCtClass(className)
                    if (ct != null) {
                        if (injectClass(ct, className)) {
                            ct.writeFile(path)
                        }
                        ct.detach()
                    }
                }
            }
        }
    }

    private def injectClass(CtClass target, String tag) {
        boolean changed = false
        if (target.isFrozen()) {
            target.defrost()
        }

        targets.findAll { target.subtypeOf(it) }.each {
//                def ctMethod = target.getDeclaredMethod("onClick", [pool.getCtClass("android.view.View")] as CtClass[])
//            ctMethod.insertAfter("cn.com.gf.gflogger.GFLogger\$\$.handle((Object)\$0, \"${ctMethod.name}\", \$args);")
            changed = true
        }

        target.nestedClasses.each {
            changed = changed || injectClass(it, tag)
        }

        changed
    }

    private static String getClassName(String rootPath, String filePath) {
        def rp = getRelatedPath(rootPath, filePath)
        rp = rp.substring(0, rp.lastIndexOf('.'))
        return rp.replaceAll('/', '.')
    }

    private static String getRelatedPath(String rootPath, String filePath) {
        return filePath.replaceFirst("$rootPath/", "")
    }
}
