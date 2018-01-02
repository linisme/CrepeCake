package net.idik.crepecake.api;

public interface InvocationHandler {

    Object getCaller();

    Object call(Object... args);

}
