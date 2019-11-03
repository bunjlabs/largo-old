package com.bunjlabs.largo.types;

class LargoValuePrototype extends LargoPrototype {

    LargoValuePrototype() {
        set(LargoString.from("toString"), LargoFunction.fromFunction(this::convertString));
    }

    private LargoValue convertString(LargoValue value) {
        return value.asString();
    }
}
