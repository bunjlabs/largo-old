package com.bunjlabs.largo.types;

public class LargoArrayPrototype extends LargoPrototype {

    LargoArrayPrototype() {
        set(LargoString.from("length"), LargoFunction.fromFunction(this::length));
        set(LargoString.from("get"), LargoFunction.fromBiFunction(this::getProperty));
        set(LargoString.from("set"), LargoFunction.fromVarArgFunction(this::setProperty));
    }

    private LargoValue length(LargoValue value) {
        return value.asNumber();
    }

    private LargoValue getProperty(LargoValue value, LargoValue key) {
        return value.get(key);
    }

    private LargoValue setProperty(LargoValue value, LargoValue[] args) {
        if (args.length == 1) {
            value.set(args[0], LargoUndefined.UNDEFINED);
        } else if (args.length > 1) {
            value.set(args[0], args[1]);
        }
        return value;
    }
}
