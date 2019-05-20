package com.bunjlabs.largo.lib.functions;

import com.bunjlabs.largo.types.LargoValue;

import java.util.function.Function;

public class FunctionLargoFunction extends JavaLargoFunction {
    private final Function<LargoValue, LargoValue> function;

    public FunctionLargoFunction(Function<LargoValue, LargoValue> function) {
        this.function = function;
    }

    @Override
    public LargoValue call(LargoValue context, LargoValue... args) {
        return function.apply(context);
    }
}
