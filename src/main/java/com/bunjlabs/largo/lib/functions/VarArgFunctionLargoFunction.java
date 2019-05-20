package com.bunjlabs.largo.lib.functions;

import com.bunjlabs.largo.types.LargoValue;

public class VarArgFunctionLargoFunction extends JavaLargoFunction {
    private final VarArgFunction<LargoValue, LargoValue, LargoValue> function;

    public VarArgFunctionLargoFunction(VarArgFunction<LargoValue, LargoValue, LargoValue> function) {
        this.function = function;
    }

    @Override
    public LargoValue call(LargoValue context, LargoValue... values) {
        return function.apply(context, values);
    }
}
