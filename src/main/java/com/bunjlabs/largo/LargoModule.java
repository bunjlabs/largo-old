package com.bunjlabs.largo;

import com.bunjlabs.largo.types.LargoObject;
import com.bunjlabs.largo.types.LargoValue;

public class LargoModule {

    private final LargoObject exports;

    public LargoModule() {
        this.exports = LargoObject.empty();
    }

    public LargoModule(LargoObject exports) {
        this.exports = exports;
    }

    public void export(LargoValue name, LargoValue value) {
        exports.set(name, value);
    }

    public LargoObject getExports() {
        return exports;
    }
}
