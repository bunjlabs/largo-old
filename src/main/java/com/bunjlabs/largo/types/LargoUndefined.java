package com.bunjlabs.largo.types;

public class LargoUndefined extends LargoValue {
    public static final LargoUndefined UNDEFINED = new LargoUndefined();

    private LargoUndefined() {
    }

    @Override
    public LargoType getType() {
        return LargoType.UNDEFINED;
    }

    public String asJString() {
        return "undefined";
    }


    @Override
    public LargoValue eq(LargoValue rv) {
        return LargoBoolean.from(rv instanceof LargoUndefined);
    }
}
