package com.bunjlabs.largo;

public class LargoRuntimeBuilder {

    private LargoEnvironment environment;
    private LargoLoader loader;

    public LargoRuntimeBuilder withEnvironment(LargoEnvironment environment) {
        this.environment = environment;
        return this;
    }

    public LargoRuntimeBuilder withLoader(LargoLoader loader) {
        this.loader = loader;
        return this;
    }

    public LargoRuntime build() {
        if(environment == null) {
            environment = new DefaultLargoEnvironment();
        }

        if(loader == null) {
            loader = new FileLargoLoader();
        }

        return new DefaultLargoRuntime(environment, loader);
    }
}
