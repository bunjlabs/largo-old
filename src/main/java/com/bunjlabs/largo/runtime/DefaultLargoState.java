package com.bunjlabs.largo.runtime;

import com.bunjlabs.largo.types.LargoUndefined;
import com.bunjlabs.largo.types.LargoValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class DefaultLargoState implements LargoState {
    private final Map<Integer, LargoValue> localVariables = new HashMap<>();
    private final Stack<LargoValue> stack = new Stack<>();

    private final LargoRuntimeConstraints constraints;

    public DefaultLargoState(LargoRuntimeConstraints constraints) {
        this.constraints = constraints;
    }

    @Override
    public void init(int count) {
        for (int i = 0; i < count; i++) {
            localVariables.put(i, LargoUndefined.UNDEFINED);
        }
    }

    @Override
    public void clear() {
        stack.clear();
        localVariables.clear();
    }


    @Override
    public LargoValue stackPush(LargoValue value) throws LargoRuntimeException {
        if (stack.size() + 1 > constraints.getMaxStackSize()) {
            throw new LargoRuntimeException("Stack overflow");
        }

        return stack.push(value);
    }

    @Override
    public LargoValue stackPop() throws LargoRuntimeException {
        return stack.pop();
    }

    @Override
    public LargoValue stackPeek() throws LargoRuntimeException {
        return stack.peek();
    }

    @Override
    public void setLocal(int id, LargoValue value) {
        if (localVariables.containsKey(id)) {
            localVariables.put(id, value);
        }
    }

    @Override
    public LargoValue getLocal(int id) {
        return localVariables.get(id);
    }

}
