package com.bunjlabs.largo.types;

import java.util.HashMap;
import java.util.Map;

public class LargoObject extends LargoValue {
    private static final LargoPrototype PROTOTYPE = new Prototype();

    private final Map<LargoValue, LargoValue> map;

    public static LargoObject empty() {
        return new LargoObject();
    }

    private LargoObject() {
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
        return PROTOTYPE;
    }

    @Override
    public LargoValue get(LargoValue key) {
        LargoValue value = map.get(key);
        return value != null ? value : super.get(key);
    }

    public LargoValue getOwn(LargoValue key) {
        LargoValue value = map.get(key);
        return value != null ? value : LargoUndefined.UNDEFINED;
    }

    @Override
    public void set(LargoValue key, LargoValue value) {
        map.put(key, value);
    }

    private static class Prototype extends LargoPrototype {

        Prototype() {
            set(LargoString.from("hasProperty"), LargoFunction.fromBiFunction(this::hasProperty));
            set(LargoString.from("get"), LargoFunction.fromBiFunction(this::getProperty));
            set(LargoString.from("set"), LargoFunction.fromVarArgFunction(this::setProperty));
            setProperty("toString", LargoFunction.fromFunction(this::convertString));
        }

        private LargoValue hasProperty(LargoValue thisRef, LargoValue property) {
            return LargoBoolean.from(thisRef.get(property).getType() != LargoType.UNDEFINED);
        }

        private LargoValue getProperty(LargoValue thisRef, LargoValue key) {
            return thisRef.get(key);
        }

        private LargoValue setProperty(LargoValue thisRef, LargoValue[] args) {
            if (args.length == 1) {
                thisRef.set(args[0], LargoUndefined.UNDEFINED);
            } else if (args.length > 1) {
                thisRef.set(args[0], args[1]);
            }
            return thisRef;
        }

        private LargoValue convertString(LargoValue thisRef) {
            return thisRef.asString();
        }
    }
}
