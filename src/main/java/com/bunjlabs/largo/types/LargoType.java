package com.bunjlabs.largo.types;

public enum LargoType {
    NULL("null"),
    UNDEFINED("undefined"),
    BOOLEAN("boolean"),
    NUMBER("number"),
    STRING("string"),
    ARRAY("array"),
    OBJECT("object"),
    FUNCTION("function");

    private final String typeName;

    LargoType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
