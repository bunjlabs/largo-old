package com.bunjlabs.largo;

import com.bunjlabs.largo.types.LargoContext;

public interface LargoEnvironment {

    LargoRuntimeConstraints getConstraints();

    LargoContext getContext();

}
