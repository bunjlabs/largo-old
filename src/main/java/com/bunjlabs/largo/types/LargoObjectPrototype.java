package com.bunjlabs.largo.types;

class LargoObjectPrototype extends LargoPrototype {

    LargoObjectPrototype() {
        set(LargoString.from("hasProperty"), LargoFunction.fromBiFunction(this::hasProperty));
        set(LargoString.from("get"), LargoFunction.fromBiFunction(this::getProperty));
        set(LargoString.from("set"), LargoFunction.fromVarArgFunction(this::setProperty));
    }


    private LargoValue hasProperty(LargoValue value, LargoValue property) {
        return LargoBoolean.from(value.get(property).getType() != LargoType.UNDEFINED);
    }

    private LargoValue getProperty(LargoValue value, LargoValue key) {
        return value.get(key);
    }

    private LargoValue setProperty(LargoValue value, LargoValue[] args) {
        if(args.length == 1) {
            value.set(args[0], LargoUndefined.UNDEFINED);
        } else if(args.length > 1) {
            value.set(args[0], args[1]);
        }
        return value;
    }
}
