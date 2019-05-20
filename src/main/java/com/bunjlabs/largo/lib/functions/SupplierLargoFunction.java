package com.bunjlabs.largo.lib.functions;

import com.bunjlabs.largo.types.LargoValue;

import java.util.function.Supplier;

public class SupplierLargoFunction extends JavaLargoFunction {
    private final Supplier<LargoValue> supplier;

    public SupplierLargoFunction(Supplier<LargoValue> supplier) {
        this.supplier = supplier;
    }

    @Override
    public LargoValue call(LargoValue context, LargoValue... args) {
        return supplier.get();
    }
}
