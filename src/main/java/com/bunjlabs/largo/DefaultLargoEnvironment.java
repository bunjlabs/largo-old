package com.bunjlabs.largo;

import com.bunjlabs.largo.lib.MathLib;
import com.bunjlabs.largo.types.LargoContext;
import com.bunjlabs.largo.types.LargoObject;
import com.bunjlabs.largo.types.LargoString;
import com.bunjlabs.largo.types.LargoValue;

public class DefaultLargoEnvironment implements LargoEnvironment {

    private final LargoContext context = LargoContext.create();

    private final LargoRuntimeConstraints constraints;

    public DefaultLargoEnvironment() {
        this.constraints = new DefaultLargoRuntimeConstraints();
    }

    public DefaultLargoEnvironment(LargoRuntimeConstraints constraints) {
        this.constraints = constraints;
    }

    public void export(String id, LargoValue value) {
        context.set(LargoString.from(id), value);
    }

    @Override
    public LargoRuntimeConstraints getConstraints() {
        return constraints;
    }

    @Override
    public LargoContext getContext() {
        return context;
    }
}
