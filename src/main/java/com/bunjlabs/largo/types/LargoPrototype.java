package com.bunjlabs.largo.types;

import java.util.HashMap;

public abstract class LargoPrototype extends LargoObject {

    private static final LargoPrototype EMPTY = new LargoPrototype() {
    };

    public static LargoPrototype epmty() {
        return EMPTY;
    }

    LargoPrototype() {
        super(new HashMap<>());
    }

    @Override
    public LargoValue get(LargoValue key) {
        return getOwn(key);
    }

    void setProperty(String name, LargoValue value) {
        super.set(LargoString.from(name), value);
    }

    @Override
    public void set(LargoValue key, LargoValue value) {
        // read only
    }
}
