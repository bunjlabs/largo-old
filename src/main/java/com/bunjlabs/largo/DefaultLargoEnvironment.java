package com.bunjlabs.largo;

import com.bunjlabs.largo.types.LargoValue;

import java.util.HashMap;
import java.util.Map;

public class DefaultLargoEnvironment implements LargoEnvironment {

    private final Map<String, LargoModule> modules = new HashMap<>();
    private final LargoConstraints constraints;

    public DefaultLargoEnvironment() {
        this.constraints = new DefaultLargoConstraints();
    }

    public DefaultLargoEnvironment(LargoConstraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public void addModule(String id, LargoModule module) {
        modules.put(id, module);
    }

    @Override
    public LargoModule importModule(String id) {
        return modules.get(id);
    }

    @Override
    public LargoConstraints getConstraints() {
        return constraints;
    }

}
