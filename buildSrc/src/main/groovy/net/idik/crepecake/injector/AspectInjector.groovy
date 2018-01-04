package net.idik.crepecake.injector

import javassist.*
import org.apache.commons.codec.digest.DigestUtils

class AspectInjector {

    private static
    final String INVOCATION_HANDLE_CLASSNAME = 'net.idik.crepecake.api.InvocationHandler'
    private static final String INSTRUCTION = "net.idik.crepecake.AspectInstruction"
    private static
    final int CHECKING_MODIFIERS = Modifier.STATIC | Modifier.PRIVATE | Modifier.PUBLIC | Modifier.PROTECTED

    private static ClassPool pool = ClassPool.getDefault()

    private Object[] configs
    private String[] processors
    private String path

    private ClassLoader runnerLoader = new Loader(pool)

    AspectInjector() {
        def instruction = pool.getCtClass(INSTRUCTION).toClass(runnerLoader, null)
        def field = instruction.getDeclaredField('processors')
        field.setAccessible(true)
        processors = field.get(instruction) as String[]
        configs = new Object[processors.length]
        field = instruction.getDeclaredField('configs')
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
            configs[index] = toClass(ct).newInstance()
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

        configs.eachWithIndex { it, index ->
            Class targetClazz = toClass(target)
            if (isHook(it, targetClazz)) {
                if (target.isFrozen()) {
                    target.defrost()
                }
                def processor = pool.getCtClass(processors[index])
                if (processor.isFrozen()) {
                    processor.defrost()
                }
                if (injectIt(target, processor)) {
                    processor.writeFile(path)
                }
                processor.detach()
                changed = true
            }
        }
        changed
    }

    private Class toClass(CtClass target) {
        def targetClazz = runnerLoader.loadClass(target.getName())
        if (targetClazz == null) {
            targetClazz = target.toClass(runnerLoader, null);
        }
        targetClazz
    }


    private def injectIt(CtClass target, CtClass processor) {
        boolean processorChanged = false

        processor.getDeclaredMethods().findAll {
            it.parameterTypes.length > 0 && INVOCATION_HANDLE_CLASSNAME == it.parameterTypes[0].name
        }
        .each {
            CtClass[] parameters = it.parameterTypes
            parameters -= it.parameterTypes[0]

            def method = prepareTargetMethod(target, processor, it, parameters)

            if (method != null && validate(method, it)) {

                if ((it.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC) {
                    it.setModifiers(it.getModifiers() & ~Modifier.PRIVATE & ~Modifier.PROTECTED | Modifier.PUBLIC)
                    processorChanged |= true
                }

                def newMethodName = "_____${processor.name.replaceAll("\\.", "_")}_${method.name}_${System.currentTimeMillis()}"
                target.addMethod(CtNewMethod.copy(method, newMethodName, target, null))
                CtClass invocationHandler = prepareInvocationHandler(target, processor, it)
                String paramStr = ""
                parameters.eachWithIndex { entry, i ->
                    paramStr += "(${entry.name})args[$i], "
                }
                if (paramStr.endsWith(", ")) {
                    paramStr = paramStr.substring(0, paramStr.length() - 2)
                }
                def proxyMethod = CtNewMethod.make("protected Object call(Object[] args) { " +
                        "${target.name} target = ((${target.name})getCaller()); " +
                        (returnVoid(method) ? "target.${newMethodName}($paramStr); return null;" : "return target.${newMethodName}($paramStr);") +
                        "}", invocationHandler)
                invocationHandler.addMethod(proxyMethod)

                def processorFieldName = "___" + processor.name.replaceAll("\\.", "_")
                try {
                    target.getDeclaredField(processorFieldName)
                } catch (NotFoundException e) {
                    target.addField(CtField.make("private ${processor.name} $processorFieldName = new ${processor.name}();", target))
                }
                paramStr = ""
                parameters.eachWithIndex { entry, i ->
                    paramStr += "\$${i + 1}, "
                }
                if (paramStr.length() > 0) {
                    paramStr = ", $paramStr"
                    paramStr = paramStr.substring(0, paramStr.length() - 2)
                }
                if (returnVoid(method)) {
                    method.setBody(" $processorFieldName.${method.name}(new ${invocationHandler.name}(\$0)$paramStr); ")
                } else {
                    method.setBody("return $processorFieldName.${method.name}(new ${invocationHandler.name}(\$0)$paramStr); ")
                }

                invocationHandler.writeFile(path)
                invocationHandler.detach()
            }


        }

        processorChanged

    }

    private
    static CtClass prepareInvocationHandler(CtClass target, CtClass processor, CtMethod injectMethod) {
        CtClass invocationHandlerInterface = pool.getCtClass("net.idik.crepecake.api.InvocationHandler")
        def invocationHandlerClassName = "${target.name}\$${processor.name.replaceAll('\\.', '_')}_${injectMethod.name}_${DigestUtils.md5Hex(injectMethod.longName)}_InvocationHandler"
        CtClass invocationHandler = pool.getOrNull(invocationHandlerClassName)
        if (invocationHandler == null) {
            invocationHandler = pool.makeClass(invocationHandlerClassName)
            invocationHandler.setSuperclass(invocationHandlerInterface)
            invocationHandler.addConstructor(CtNewConstructor.make([pool.getCtClass("java.lang.Object")] as CtClass[], null, "super(\$1);", invocationHandler))
        } else {
            if (invocationHandler.isFrozen()) {
                invocationHandler.defrost()
            }
            invocationHandler.getDeclaredMethods().each {
                invocationHandler.removeMethod(it)
            }
            invocationHandler.getDeclaredFields().each {
                invocationHandler.removeField(it)
            }
        }
        invocationHandler
    }

    private static
    def prepareTargetMethod(CtClass target, CtClass processor, CtMethod injectMethod, CtClass[] parameters) {

        CtMethod result = null
        try {
            result = target.getDeclaredMethod(injectMethod.name, parameters)
        } catch (NotFoundException e) {
            try {
                result = target.getMethod(injectMethod.name, injectMethod.signature.replace('(Lnet/idik/crepecake/api/InvocationHandler;', '('))
            } catch (NotFoundException ex) {

            }
            if (result != null) {
                if (((result.getModifiers() & Modifier.PRIVATE) == Modifier.PRIVATE) || !validate(result, injectMethod)) {
                    result = null
                }
            }
            if (result != null) {
                result = CtNewMethod.copy(result, target, null)
                target.addMethod(result)
                def paramStr = ""
                parameters.eachWithIndex { entry, i ->
                    paramStr += "\$${i + 1}, "
                }
                if (paramStr.length() > 0) {
                    paramStr = paramStr.substring(0, paramStr.length() - 2)
                }
                if (returnVoid(result)) {
                    result.setBody("super.${result.name}($paramStr);")
                } else {
                    result.setBody("return super.${result.name}($paramStr);")
                }
            }
        }
        result
    }

    private static def isHook(Object config, Class target) {
        def method = config.getClass().getMethod("test", Class.class)
        return method.invoke(config, target)
    }

    private static boolean returnVoid(CtMethod method) {
        method.returnType == CtPrimitiveType.voidType
    }

    private static boolean validate(CtMethod originMethod, CtMethod injectMethod) {
        (originMethod.returnType == injectMethod.returnType
                && (injectMethod.getModifiers() & CHECKING_MODIFIERS) == (originMethod.getModifiers() & CHECKING_MODIFIERS))
    }

    private static String getClassName(String rootPath, String filePath) {
        def rp = getRelatedPath(rootPath, filePath)
        rp = rp.substring(0, rp.lastIndexOf('.'))
        rp.replaceAll('/', '.')
    }

    private static String getRelatedPath(String rootPath, String filePath) {
        filePath.replaceFirst("$rootPath/", "")
    }
}
