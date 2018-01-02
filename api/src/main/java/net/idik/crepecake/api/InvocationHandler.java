package net.idik.crepecake.api;

public abstract class InvocationHandler {

    private Object caller;

    public InvocationHandler(Object caller) {
        this.caller = caller;
    }

    public Object getCaller() {
        return caller;
    }

    public Object invoke(Object... args) {
        return call(args);
    }

    protected abstract Object call(Object[] args);

}
