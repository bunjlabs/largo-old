package com.bunjlabs.largo.types;

public abstract class LargoNumber extends LargoValue {
    private static final LargoPrototype PROTOTYPE = new Prototype();

    public static LargoNumber from(int value) {
        return LargoInteger.from(value);
    }

    public static LargoNumber from(double value) {
        if ((value % 1) == 0 && value <= Integer.MAX_VALUE) {
            return LargoInteger.from((int) value);
        } else {
            return LargoDouble.from(value);
        }
    }

    @Override
    public LargoType getType() {
        return LargoType.NUMBER;
    }

    @Override
    public LargoPrototype getPrototype() {
        return PROTOTYPE;
    }

    private static class Prototype extends LargoPrototype {

        Prototype() {
            setProperty("valueOf", LargoFunction.fromBiFunction(this::valueOf));
            setProperty("toString", LargoFunction.fromFunction(this::convertString));
        }

        private LargoValue valueOf(LargoValue thisRef, LargoValue value) {
            return value.asNumber();
        }

        private LargoValue convertString(LargoValue thisRef) {
            return thisRef.asString();
        }
    }
}
