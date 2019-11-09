package com.bunjlabs.largo;


public interface LargoContext {

    LargoEnvironment getEnvironment();

    LargoRuntime getRuntime();

    LargoModule getCurrentModule();
}
