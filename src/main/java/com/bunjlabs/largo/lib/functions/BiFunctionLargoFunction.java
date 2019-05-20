package com.bunjlabs.largo.lib.functions;

import com.bunjlabs.largo.types.LargoUndefined;
import com.bunjlabs.largo.types.LargoValue;

import java.util.function.BiFunction;

public class BiFunctionLargoFunction extends JavaLargoFunction {
    private final BiFunction<LargoValue, LargoValue, LargoValue> function;

    public BiFunctionLargoFunction(BiFunction<LargoValue, LargoValue, LargoValue> function) {
        this.function = function;
    }

    @Override
    public LargoValue call(LargoValue context, LargoValue... values) {
        return function.apply(context, values.length > 0 ? values[0] : LargoUndefined.UNDEFINED);
    }
}
