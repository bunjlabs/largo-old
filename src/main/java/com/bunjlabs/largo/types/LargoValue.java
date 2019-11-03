package com.bunjlabs.largo.types;

public abstract class LargoValue {

    abstract public LargoType getType();

    public String getTypeName() {
        return getType().getTypeName();
    }

    public LargoObject getPrototype() {
        return LargoPrototypes.VALUE;
    }

    public String asJString() {
        return "[[" + getTypeName() + "]]";
    }

    public double asJDouble() {
        return Double.NaN;
    }

    public int asJInteger() {
        return 0;
    }

    public boolean asJBoolean() {
        return false;
    }

    public Object asJObject() {
        return null;
    }

    public LargoString asString() {
        return LargoString.from(asJString());
    }

    public LargoNumber asNumber() {
        return LargoDouble.NaN;
    }

    public LargoBoolean asBoolean() {
        return LargoBoolean.FALSE;
    }

    public LargoValue call(LargoValue context, LargoValue... args) {
        return LargoUndefined.UNDEFINED;
    }

    public LargoValue get(LargoValue key) {
        return getPrototype().get(key);
    }

    public void set(LargoValue key, LargoValue value) {

    }

    public void push(LargoValue value) {

    }

    public LargoValue and(LargoValue rv) {
        return LargoBoolean.from(asJBoolean() && rv.asJBoolean());
    }

    public LargoValue or(LargoValue rv) {
        return LargoBoolean.from(asJBoolean() || rv.asJBoolean());
    }

    public LargoValue not() {
        return LargoBoolean.from(!asJBoolean());
    }

    public LargoValue neg() {
        return LargoDouble.NaN;
    }

    public LargoValue pos() {
        return LargoDouble.NaN;
    }

    public LargoValue add(LargoValue rv) {
        return rv.addTo(asJString());
    }

    public LargoValue add(String rv) {
        return add(LargoString.from(rv));
    }

    public LargoValue add(boolean rv) {
        return add(LargoBoolean.from(rv));
    }

    public LargoValue add(double rv) {
        return add(LargoNumber.from(rv));
    }

    public LargoValue add(int rv) {
        return add(LargoNumber.from(rv));
    }

    LargoValue addTo(String lv) {
        return LargoString.from(lv + asJString());
    }

    LargoValue addTo(boolean lv) {
        return LargoBoolean.FALSE;
    }

    LargoValue addTo(double lv) {
        return LargoDouble.NaN;
    }

    LargoValue addTo(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue sub(LargoValue rv) {
        return LargoDouble.NaN;
    }

    public LargoValue sub(String rv) {
        return sub(LargoString.from(rv));
    }

    public LargoValue sub(boolean rv) {
        return sub(LargoBoolean.from(rv));
    }

    public LargoValue sub(double rv) {
        return sub(LargoNumber.from(rv));
    }

    public LargoValue sub(int rv) {
        return sub(LargoNumber.from(rv));
    }

    LargoValue subFrom(String lv) {
        return LargoDouble.NaN;
    }

    LargoValue subFrom(boolean lv) {
        return LargoBoolean.FALSE;
    }

    LargoValue subFrom(double lv) {
        return LargoDouble.NaN;
    }

    LargoValue subFrom(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue mul(LargoValue rv) {
        return LargoDouble.NaN;
    }

    public LargoValue mul(String rv) {
        return mul(LargoString.from(rv));
    }

    public LargoValue mul(boolean rv) {
        return mul(LargoBoolean.from(rv));
    }

    public LargoValue mul(double rv) {
        return mul(LargoNumber.from(rv));
    }

    public LargoValue mul(int rv) {
        return mul(LargoNumber.from(rv));
    }

    LargoValue mulWith(String lv) {
        return LargoDouble.NaN;
    }

    LargoValue mulWith(boolean lv) {
        return LargoDouble.NaN;
    }

    LargoValue mulWith(double lv) {
        return LargoDouble.NaN;
    }

    LargoValue mulWith(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue div(LargoValue rv) {
        return LargoDouble.NaN;
    }

    public LargoValue div(String rv) {
        return div(LargoString.from(rv));
    }

    public LargoValue div(boolean rv) {
        return div(LargoBoolean.from(rv));
    }

    public LargoValue div(double rv) {
        return div(LargoNumber.from(rv));
    }

    public LargoValue div(int rv) {
        return div(LargoNumber.from(rv));
    }

    LargoValue divInto(String lv) {
        return LargoDouble.NaN;
    }

    LargoValue divInto(boolean lv) {
        return LargoDouble.NaN;
    }

    LargoValue divInto(double lv) {
        return LargoDouble.NaN;
    }

    LargoValue divInto(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue mod(LargoValue rv) {
        return LargoDouble.NaN;
    }

    public LargoValue mod(String rv) {
        return mod(LargoString.from(rv));
    }

    public LargoValue mod(boolean rv) {
        return mod(LargoBoolean.from(rv));
    }

    public LargoValue mod(double rv) {
        return mod(LargoNumber.from(rv));
    }

    public LargoValue mod(int rv) {
        return mod(LargoNumber.from(rv));
    }

    LargoValue modFrom(String lv) {
        return LargoDouble.NaN;
    }

    LargoValue modFrom(boolean lv) {
        return LargoDouble.NaN;
    }

    LargoValue modFrom(double lv) {
        return LargoDouble.NaN;
    }

    LargoValue modFrom(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue eq(LargoValue rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue eq(String rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue eq(boolean rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue eq(double rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue eq(int rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lt(LargoValue rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lt(String rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lt(boolean rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lt(double rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lt(int rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lteq(LargoValue rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lteq(String rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lteq(boolean rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lteq(double rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue lteq(int rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gt(LargoValue rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gt(String rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gt(boolean rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gt(double rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gt(int rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gteq(LargoValue rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gteq(String rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gteq(boolean rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gteq(double rv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue gteq(int rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof LargoValue)) return false;

        LargoValue v = (LargoValue) obj;
        return eq(v).asJBoolean();
    }

    @Override
    public int hashCode() {
        return asJString().hashCode();
    }

    private static class Prototype extends LargoPrototype {

        Prototype() {
            setProperty("toString", LargoFunction.fromFunction(this::convertString));
        }

        private LargoValue convertString(LargoValue value) {
            return value.asString();
        }

    }
}
