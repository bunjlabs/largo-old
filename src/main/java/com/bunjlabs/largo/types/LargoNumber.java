package com.bunjlabs.largo.types;

public abstract class LargoNumber extends LargoValue {

    public static LargoNumber from(double value) {
        if ((value % 1) == 0) {
            return LargoInteger.from((int) value);
        } else {
            return LargoDouble.from(value);
        }
    }

    @Override
    public LargoType getType() {
        return LargoType.NUMBER;
    }
}
