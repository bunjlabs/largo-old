package com.bunjlabs.largo.types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LargoArray extends LargoValue {
    private static final Prototype prototype = new Prototype();
    private final List<LargoValue> list = new ArrayList<>();

    @Override
    public String asJString() {
        return "[" + list.stream().map(LargoValue::asJString).collect(Collectors.joining(",")) + "]";
    }

    @Override
    public LargoType getType() {
        return LargoType.ARRAY;
    }

    @Override
    public Prototype getPrototype() {
        return prototype;
    }

    @Override
    public LargoValue get(LargoValue key) {
        LargoValue value = list.get(key.asJInteger());
        return value != null ? value : super.get(key);
    }

    @Override
    public void set(LargoValue key, LargoValue value) {
        list.set(key.asJInteger(), value);
    }

    @Override
    public void push(LargoValue value) {
        list.add(value);
    }

    private static class Prototype extends LargoPrototype {

        Prototype() {
            setProperty("length", LargoFunction.fromFunction(this::length));
        }

        private LargoValue length(LargoValue value) {
            return LargoNumber.from(0);
        }
    }
}
