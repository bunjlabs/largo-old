package com.bunjlabs.largo.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LargoObject extends LargoValue {
    public static final LargoObject EMPTY = new LargoObject(Collections.EMPTY_MAP);

    private final Map<LargoValue, LargoValue> map;

    public LargoObject() {
        this.map = new HashMap<>();
    }

    public LargoObject(Map<LargoValue, LargoValue> map) {
        this.map = map;
    }

    public static LargoObject create() {
        return new LargoObject(new HashMap<>());
    }

    @Override
    public LargoType getType() {
        return LargoType.OBJECT;
    }

    @Override
    public LargoValue get(LargoValue key) {
        LargoValue value = map.get(key);
        return value != null ? value : LargoUndefined.UNDEFINED;
    }

    @Override
    public void set(LargoValue key, LargoValue value) {
        map.put(key, value);
    }
}
