package com.bunjlabs.largo.types;

import com.bunjlabs.largo.types.prototype.LargoPrototype;

public abstract class LargoValue {

    abstract public LargoType getType();

    public String getTypeName() {
        return getType().getTypeName();
    }

    public LargoPrototype getPrototype() {
        return LargoPrototype.EMPTY;
    }

    public String asJString() {
        return "[" + getTypeName() + "]";
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

    public LargoString asString() {
        return LargoString.from(asJString());
    }

    public LargoNumber asNumber() {
        return LargoDouble.NaN;
    }

    public LargoBoolean asBoolean() {
        return LargoBoolean.FALSE;
    }

    public LargoValue call(LargoValue... args) {
        return LargoUndefined.UNDEFINED;
    }

    public LargoValue get(LargoValue key) {
        return getPrototype().get(this, key);
    }

    public void set(LargoValue key, LargoValue value) {

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

    public LargoValue addTo(String lv) {
        return LargoString.from(lv + asJString());
    }

    public LargoValue addTo(boolean lv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue addTo(double lv) {
        return LargoDouble.NaN;
    }

    public LargoValue addTo(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue sub(LargoValue rv) {
        return LargoDouble.NaN;
    }

    public LargoValue subFrom(String lv) {
        return LargoDouble.NaN;
    }

    public LargoValue subFrom(boolean lv) {
        return LargoBoolean.FALSE;
    }

    public LargoValue subFrom(double lv) {
        return LargoDouble.NaN;
    }

    public LargoValue subFrom(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue mul(LargoValue rv) {
        return LargoDouble.NaN;
    }

    public LargoValue mulWith(String lv) {
        return LargoDouble.NaN;
    }

    public LargoValue mulWith(boolean lv) {
        return LargoDouble.NaN;
    }

    public LargoValue mulWith(double lv) {
        return LargoDouble.NaN;
    }

    public LargoValue mulWith(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue div(LargoValue rv) {
        return LargoDouble.NaN;
    }

    public LargoValue divInto(String lv) {
        return LargoDouble.NaN;
    }

    public LargoValue divInto(boolean lv) {
        return LargoDouble.NaN;
    }

    public LargoValue divInto(double lv) {
        return LargoDouble.NaN;
    }

    public LargoValue divInto(int lv) {
        return LargoDouble.NaN;
    }

    public LargoValue mod(LargoValue rv) {
        return LargoDouble.NaN;
    }

    public LargoValue modFrom(String lv) {
        return LargoDouble.NaN;
    }

    public LargoValue modFrom(boolean lv) {
        return LargoDouble.NaN;
    }

    public LargoValue modFrom(double lv) {
        return LargoDouble.NaN;
    }

    public LargoValue modFrom(int lv) {
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
}
