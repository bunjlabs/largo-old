package com.bunjlabs.largo;

import com.bunjlabs.largo.types.LargoClosure;
import com.bunjlabs.largo.types.LargoString;

import java.io.Reader;

public class DefaultLargoRuntime implements LargoRuntime {

    private final LargoEnvironment environment;
    private final LargoLoader loader;

    DefaultLargoRuntime(LargoEnvironment environment, LargoLoader loader) {
        this.environment = environment;
        this.loader = loader;
    }

    @Override
    public LargoEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public LargoLoader getLoader() {
        return loader;
    }

    @Override
    public LargoModule load(String id) throws LargoLoaderException, LargoRuntimeException {
        Blueprint blueprint = loader.load(id);
        return load(id, blueprint);
    }

    @Override
    public LargoModule load(String id, String source) throws LargoLoaderException, LargoRuntimeException {
        Blueprint blueprint = loader.loadSource(source);
        return load(id, blueprint);
    }

    @Override
    public LargoModule load(String id, Reader source) throws LargoLoaderException, LargoRuntimeException {
        Blueprint blueprint = loader.loadSource(source);
        return load(id, blueprint);
    }

    @Override
    public LargoModule load(String id, Blueprint blueprint) throws LargoRuntimeException {
        LargoConstraints constraints = environment.getConstraints();

        if (blueprint.getInstructionsCount() > constraints.getMaxCodeLength()) {
            throw new LargoRuntimeException("Maximum code length exceeded " +
                    "(limit: " + constraints.getMaxCodeLength() +
                    " actual: " + blueprint.getInstructionsCount());
        }

        if (blueprint.getConstantPool().length > constraints.getMaxConstantPoolSize()) {
            throw new LargoRuntimeException("Maximum constant pool size exceeded " +
                    "(limit: " + constraints.getMaxConstantPoolSize() +
                    " actual: " + blueprint.getConstantPool().length);
        }

        if (blueprint.getVariablesCount() > constraints.getMaxVariables()) {
            throw new LargoRuntimeException("Maximum variables count exceeded " +
                    "(limit: " + constraints.getMaxVariables() +
                    " actual: " + blueprint.getVariablesCount());
        }

        if (blueprint.getCallStackSize() > constraints.getMaxStackSize()) {
            throw new LargoRuntimeException("Maximum stack size exceeded " +
                    "(limit: " + constraints.getMaxStackSize() +
                    " actual: " + blueprint.getCallStackSize());
        }

        LargoClosure closure = LargoClosure.from(blueprint);
        LargoModule module = new LargoModule();
        LargoContext context = new Context(module);

        closure.call(context, closure);

        environment.addModule(id, module);

        return module;
    }

    private class Context implements LargoContext {

        private final LargoModule module;

        Context(LargoModule module) {
            this.module = module;
        }

        @Override
        public LargoEnvironment getEnvironment() {
            return environment;
        }

        @Override
        public LargoRuntime getRuntime() {
            return DefaultLargoRuntime.this;
        }

        @Override
        public LargoModule getCurrentModule() {
            return module;
        }
    }
}
