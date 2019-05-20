package com.bunjlabs.largo.types;

public abstract class LargoFunction extends LargoValue {

    private final LargoValue context;

    protected LargoFunction() {
        this.context = this;
    }

    private LargoFunction(LargoValue context) {
        this.context = context;
    }

    public LargoType getType() {
        return LargoType.FUNCTION;
    }

    public LargoFunction bind(LargoValue context) {
        return new LargoFunction(context) {
            @Override
            public LargoValue call(LargoValue context, LargoValue... args) {
                return LargoFunction.this.call(context, args);
            }
        };
    }

    @Override
    public final LargoValue call(LargoValue... args) {
        return call(context, args);
    }

    public abstract LargoValue call(LargoValue context, LargoValue... args);
}
