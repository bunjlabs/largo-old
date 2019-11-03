package com.bunjlabs.largo.types;

import java.util.HashMap;

public abstract class LargoPrototype extends LargoObject {

    LargoPrototype() {
        super(new HashMap<>());
    }

    public LargoValue get(LargoValue context, LargoValue key) {
        LargoValue value = get(key);

        if (value.getType() == LargoType.FUNCTION) {
            LargoFunction func = (LargoFunction) value;

            return func.bind(context);
        } else {
            return value;
        }
    }

    void setProperty(String name, LargoValue value) {
        super.set(LargoString.from(name), value);
    }

    @Override
    public void set(LargoValue key, LargoValue value) {
        // read only
    }
}
