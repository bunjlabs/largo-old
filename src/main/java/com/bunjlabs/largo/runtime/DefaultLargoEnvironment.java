package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.types.LargoValue;

import java.util.HashMap;
import java.util.Map;

public class DefaultLargoEnvironment implements LargoEnvironment {

    private final Map<String, LargoValue> exports = new HashMap<>();

    private final LargoRuntimeConstraints constraints;

    public DefaultLargoEnvironment() {
        this.constraints = new DefaultLargoRuntimeConstraints();
    }

    public DefaultLargoEnvironment(LargoRuntimeConstraints constraints) {
        this.constraints = constraints;
    }


    @Override
    public LargoRuntimeConstraints getConstraints() {
        return constraints;
    }


    @Override
    public void export(String id, LargoValue value) {
        exports.put(id, value);
    }

    @Override
    public LargoValue getExported(String id) {
        return exports.get(id);
    }
}
