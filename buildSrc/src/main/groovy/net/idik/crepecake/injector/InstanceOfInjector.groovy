package net.idik.crepecake.injector

import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.CtNewConstructor
import javassist.CtNewMethod
import javassist.Loader
import javassist.NotFoundException
import javassist.expr.ExprEditor

class InstanceOfInjector {

    private static ClassPool pool = ClassPool.getDefault()
    private static
    final String INVOCATION_HANDLE_CLASSNAME = 'net.idik.crepecake.api.InvocationHandler'

    private CtClass[] targets
    private String[] processors
    private static final String INSTRUCTION = "net.idik.crepecake.InstanceOfInstruction"
    private String path

    InstanceOfInjector() {
        def instruction = pool.getCtClass(INSTRUCTION).toClass(new Loader(pool))
        def field = instruction.getDeclaredField('processors')
        field.setAccessible(true)
        processors = field.get(instruction) as String[]
        targets = new String[processors.length]
        field = instruction.getDeclaredField('targets')
        field.setAccessible(true)
        field.get(instruction).eachWithIndex { it, index ->
            def ct = null
            while (ct == null) {
                try {
                    ct = pool.getCtClass(it)
                } catch (NotFoundException e) {
                    if (it.contains('.')) {
                        it = it.reverse().replaceFirst('\\.', '\\$').reverse()
                    }
                }
            }
            targets[index] = pool.getCtClass(it)
        }
    }

    void inject(String path) {
        this.path = path
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


        targets.eachWithIndex { it, index ->
            if (target.subtypeOf(it)) {
                def processor = pool.getCtClass(processors[index])
                injectIt(target, processor)
                changed = true
            }
        }

//        target.nestedClasses.each {
//            changed = changed || injectClass(it, tag)
//        }

        changed
    }


    private def injectIt(CtClass target, CtClass processor) {

        processor.getDeclaredMethods().findAll {
            it.parameterTypes.length > 0 && INVOCATION_HANDLE_CLASSNAME == it.parameterTypes[0].name
        }
        .each {
            CtClass[] parameters = it.parameterTypes
            parameters -= it.parameterTypes[0]

            def method = target.getDeclaredMethod(it.name, parameters)

            CtClass invocationHandlerInterface = pool.getCtClass("net.idik.crepecake.api.InvocationHandler")

            if (method != null) {
                def newMethodName = "_____${processor.name.replaceAll("\\.", "_")}_${method.name}"
                target.addMethod(CtNewMethod.copy(method, newMethodName, target, null))

                CtClass invocationHandler = pool.makeClass("${target.name}\$${processor.name.replaceAll('\\.', '_')}_InvocationHandler")
                if (invocationHandler.isFrozen()) {
                    invocationHandler.defrost()
                }
                invocationHandler.setSuperclass(invocationHandlerInterface)
                invocationHandler.addConstructor(CtNewConstructor.make([pool.getCtClass("java.lang.Object")] as CtClass[], null, "super(\$1);", invocationHandler))
                String paramStr = ""
                parameters.eachWithIndex { entry, i ->
                    paramStr += "(${entry.name})args[$i], "
                }
                if (paramStr.endsWith(", ")) {
                    paramStr = paramStr.substring(0, paramStr.length() - 2)
                }
                def proxyMethod = CtNewMethod.make("protected Object call(Object[] args) { " +
                        "${target.name} target = ((${target.name})getCaller()); " +
                        (method.returnType.name == 'void' ? "target.${newMethodName}($paramStr); return null;" : "return target.${newMethodName}($paramStr);") +
                        "}", invocationHandler)
                invocationHandler.addMethod(proxyMethod)

                def processorFieldName = "___" + processor.name.replaceAll("\\.", "_")
                target.addField(CtField.make("${processor.name} $processorFieldName = new ${processor.name}();", target))

                invocationHandler.writeFile(path)

                paramStr = ""
                parameters.eachWithIndex { CtClass entry, int i ->
                    paramStr += "\$${i + 1}"
                }

                method.setBody("""
                    $processorFieldName.${method.name}(new ${invocationHandler.name}(\$0), $paramStr);
                    """)

            }


        }

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
