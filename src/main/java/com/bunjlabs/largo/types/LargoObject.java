package com.bunjlabs.largo.types;

import java.util.HashMap;
import java.util.Map;

public class LargoObject extends LargoValue {
    private final Map<LargoValue, LargoValue> map;

    LargoObject() {
        this.map = new HashMap<>();
    }

    protected LargoObject(Map<LargoValue, LargoValue> map) {
        this.map = map;
    }

    @Override
    public LargoType getType() {
        return LargoType.OBJECT;
    }

    @Override
    public LargoPrototype getPrototype() {
        return LargoPrototypes.OBJECT;
    }

    @Override
    public LargoValue get(LargoValue key) {
        LargoValue value = map.get(key);
        return value != null ? value : super.get(key);
    }

    @Override
    public void set(LargoValue key, LargoValue value) {
        map.put(key, value);
    }
}
