package com.bunjlabs.largo.types.prototype;

import com.bunjlabs.largo.types.LargoFunction;
import com.bunjlabs.largo.types.LargoObject;
import com.bunjlabs.largo.types.LargoType;
import com.bunjlabs.largo.types.LargoValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LargoPrototype extends LargoObject {

    public static final LargoPrototype EMPTY = new LargoPrototype(Collections.EMPTY_MAP);

    public LargoPrototype() {
        super(new HashMap<>());
    }

    public LargoPrototype(Map<LargoValue, LargoValue> map) {
        super(map);
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
}
