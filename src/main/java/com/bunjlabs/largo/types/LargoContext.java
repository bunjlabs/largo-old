package com.bunjlabs.largo.types;

public class LargoContext extends LargoObject {

    private LargoContext() {
        super();
    }

    public static LargoContext create() {
        return new LargoContext();
    }
}
