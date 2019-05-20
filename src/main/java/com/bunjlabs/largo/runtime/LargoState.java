package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.types.LargoValue;

public interface LargoState {

    void init(int count);

    void clear();

    LargoValue stackPush(LargoValue value) throws LargoRuntimeException;

    LargoValue stackPop() throws LargoRuntimeException;

    LargoValue stackPeek() throws LargoRuntimeException;

    void setLocal(int id, LargoValue value);

    LargoValue getLocal(int id);

}
