package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.types.LargoValue;

public interface Script {
    OpCode[] getOpCodes();

    LargoValue[] getConstPool();

    int getLocalVariablesCount();
}
