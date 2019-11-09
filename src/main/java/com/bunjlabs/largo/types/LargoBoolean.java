package com.bunjlabs.largo.types;

public class LargoBoolean extends LargoValue {
    private static final LargoPrototype PROTOTYPE = new Prototype();
    public static final LargoBoolean FALSE = new LargoBoolean(false);
    public static final LargoBoolean TRUE = new LargoBoolean(true);
    private final boolean value;

    private LargoBoolean(boolean value) {
        this.value = value;
    }

    public static LargoBoolean from(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public LargoType getType() {
        return LargoType.BOOLEAN;
    }

    @Override
    public LargoPrototype getPrototype() {
        return PROTOTYPE;
    }

    @Override
    public String asJString() {
        return value ? "true" : "false";
    }

    @Override
    public double asJDouble() {
        return value ? 1 : 0;
    }

    @Override
    public int asJInteger() {
        return value ? 1 : 0;
    }

    @Override
    public boolean asJBoolean() {
        return value;
    }

    @Override
    public Object asJObject() {
        return value;
    }

    @Override
    public LargoString asString() {
        return LargoString.from(asJString());
    }

    @Override
    public LargoNumber asNumber() {
        return value ? LargoInteger.ONE : LargoInteger.ZERO;
    }

    @Override
    public LargoBoolean asBoolean() {
        return this;
    }

    @Override
    public LargoBoolean not() {
        return LargoBoolean.from(!value);
    }

    @Override
    public LargoValue neg() {
        return asNumber().neg();
    }

    @Override
    public LargoValue pos() {
        return asNumber();
    }

    @Override
    public LargoValue add(LargoValue rv) {
        return rv.addTo(value);
    }

    @Override
    LargoValue addTo(String lv) {
        return LargoString.from(lv + asJString());
    }

    @Override
    LargoValue addTo(boolean lv) {
        return LargoInteger.from((lv ? 1 : 0) + asJInteger());
    }

    @Override
    LargoValue addTo(double lv) {
        return LargoDouble.from(lv + asJDouble());
    }

    @Override
    LargoValue addTo(int lv) {
        return LargoInteger.from(lv + asJInteger());
    }

    @Override
    public LargoValue sub(LargoValue rb) {
        return rb.subFrom(value);
    }

    @Override
    LargoValue subFrom(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    LargoValue subFrom(boolean lv) {
        return LargoInteger.from((lv ? 1 : 0) - asJInteger());
    }

    @Override
    LargoValue subFrom(double lv) {
        return LargoDouble.from(lv - asJDouble());
    }

    @Override
    LargoValue subFrom(int lv) {
        return LargoInteger.from(lv - asJInteger());
    }

    @Override
    public LargoValue mul(LargoValue rv) {
        return rv.modFrom(value);
    }

    @Override
    LargoValue mulWith(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    LargoValue mulWith(boolean lv) {
        return LargoInteger.from((lv && value ? 1 : 0));
    }

    @Override
    LargoValue mulWith(double lv) {
        return LargoDouble.from(lv * asJDouble());
    }

    @Override
    LargoValue mulWith(int lv) {
        return LargoInteger.from(lv * asJInteger());
    }

    @Override
    public LargoValue div(LargoValue rv) {
        return rv.divInto(value);
    }

    @Override
    LargoValue divInto(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    LargoValue divInto(boolean lv) {
        return value ? from(lv).asNumber() : LargoDouble.NaN;
    }

    @Override
    LargoValue divInto(double lv) {
        return value ? LargoDouble.from(lv) : LargoDouble.NaN;
    }

    @Override
    LargoValue divInto(int lv) {
        return value ? LargoInteger.from(lv) : LargoDouble.NaN;
    }

    @Override
    public LargoValue mod(LargoValue rv) {
        return rv.modFrom(value);
    }

    @Override
    LargoValue modFrom(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    LargoValue modFrom(boolean lv) {
        return value ? LargoInteger.ZERO : LargoDouble.NaN;
    }

    @Override
    LargoValue modFrom(double lv) {
        return value ? LargoDouble.from(lv % 1) : LargoDouble.NaN;
    }

    @Override
    LargoValue modFrom(int lv) {
        return value ? LargoInteger.ZERO : LargoDouble.NaN;
    }

    @Override
    public LargoValue eq(LargoValue rv) {
        return rv.eq(value);
    }

    @Override
    public LargoValue eq(String rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue eq(boolean rv) {
        return LargoBoolean.from(rv == value);
    }

    @Override
    public LargoValue eq(double rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue eq(int rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue lt(LargoValue rv) {
        return rv.gt(value);
    }

    @Override
    public LargoValue lt(String rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue lt(boolean rv) {
        return LargoBoolean.from(!value && rv);
    }

    @Override
    public LargoValue lt(double rv) {
        return LargoBoolean.from(asJInteger() < rv);
    }

    @Override
    public LargoValue lt(int rv) {
        return LargoBoolean.from(asJInteger() < rv);
    }

    @Override
    public LargoValue lteq(LargoValue rv) {
        return rv.gteq(value);
    }

    @Override
    public LargoValue lteq(String rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue lteq(boolean rv) {
        return LargoBoolean.TRUE;
    }

    @Override
    public LargoValue lteq(double rv) {
        return LargoBoolean.from(asJInteger() <= rv);
    }

    @Override
    public LargoValue lteq(int rv) {
        return LargoBoolean.from(asJInteger() <= rv);
    }

    @Override
    public LargoValue gt(LargoValue rv) {
        return rv.lt(value);
    }

    @Override
    public LargoValue gt(String rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gt(boolean rv) {
        return LargoBoolean.from(value && !rv);
    }

    @Override
    public LargoValue gt(double rv) {
        return LargoBoolean.from(asJInteger() > rv);
    }

    @Override
    public LargoValue gt(int rv) {
        return LargoBoolean.from(asJInteger() > rv);
    }

    @Override
    public LargoValue gteq(LargoValue rv) {
        return lteq(value);
    }

    @Override
    public LargoValue gteq(String rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gteq(boolean rv) {
        return LargoBoolean.TRUE;
    }

    @Override
    public LargoValue gteq(double rv) {
        return LargoBoolean.from(asJInteger() >= rv);
    }

    @Override
    public LargoValue gteq(int rv) {
        return LargoBoolean.from(asJInteger() >= rv);
    }

    private static class Prototype extends LargoPrototype {

        Prototype() {
            setProperty("valueOf", LargoFunction.fromBiFunction(this::valueOf));
            setProperty("toString", LargoFunction.fromFunction(this::convertString));
        }

        private LargoValue valueOf(LargoValue thisRef, LargoValue value) {
            return value.asBoolean();
        }

        private LargoValue convertString(LargoValue thisRef) {
            return thisRef.asString();
        }
    }
}
