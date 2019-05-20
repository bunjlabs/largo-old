package com.bunjlabs.largo.types;

public class LargoNull extends LargoValue {
    public static final LargoNull NULL = new LargoNull();

    private LargoNull() {
    }

    @Override
    public LargoType getType() {
        return LargoType.NULL;
    }

    public String asJString() {
        return "null";
    }

    @Override
    public LargoNumber asNumber() {
        return LargoInteger.ZERO;
    }

    @Override
    public LargoValue neg() {
        return LargoInteger.ONE;
    }

    @Override
    public LargoValue pos() {
        return LargoInteger.ZERO;
    }

    @Override
    public LargoValue eq(LargoValue rv) {
        return LargoBoolean.from(rv instanceof LargoNull);
    }
}
