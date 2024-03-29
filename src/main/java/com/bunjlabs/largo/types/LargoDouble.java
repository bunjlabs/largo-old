package com.bunjlabs.largo.types;

public class LargoDouble extends LargoNumber {
    public static final LargoDouble NaN = new LargoDouble(Double.NaN);
    public static final LargoDouble MAX_VALUE = new LargoDouble(Double.MAX_VALUE);
    public static final LargoDouble MIN_VALUE = new LargoDouble(Double.MIN_VALUE);
    public static final LargoDouble ZERO = new LargoDouble(0);

    private final double value;

    private LargoDouble(double value) {
        this.value = value;
    }

    public static LargoDouble from(double value) {
        return new LargoDouble(value);
    }

    @Override
    public String asJString() {
        return String.valueOf(value);
    }

    @Override
    public double asJDouble() {
        return value;
    }

    @Override
    public int asJInteger() {
        return (int) value;
    }

    @Override
    public boolean asJBoolean() {
        return value > 0;
    }

    @Override
    public Object asJObject() {
        return value;
    }

    @Override
    public LargoNumber asNumber() {
        return this;
    }

    @Override
    public LargoBoolean asBoolean() {
        return LargoBoolean.from(asJBoolean());
    }

    @Override
    public LargoValue not() {
        return neg();
    }

    @Override
    public LargoValue neg() {
        return LargoDouble.from(-value);
    }

    @Override
    public LargoValue pos() {
        return this;
    }

    @Override
    public LargoValue add(LargoValue rv) {
        return rv.addTo(value);
    }

    @Override
    public LargoValue addTo(String lv) {
        return LargoString.from(lv + value);
    }

    @Override
    public LargoValue addTo(boolean lv) {
        return lv ? LargoDouble.from(1 + value) : this;
    }

    @Override
    public LargoValue addTo(double lv) {
        return LargoDouble.from(lv + value);
    }

    @Override
    public LargoValue addTo(int lv) {
        return LargoDouble.from(lv + value);
    }

    @Override
    public LargoValue sub(LargoValue rv) {
        return rv.subFrom(value);
    }

    @Override
    public LargoValue subFrom(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue subFrom(boolean lv) {
        return LargoDouble.from((lv ? 1 : 0) - value);
    }

    @Override
    public LargoValue subFrom(double lv) {
        return LargoDouble.from(lv - value);
    }

    @Override
    public LargoValue subFrom(int lv) {
        return LargoDouble.from(lv - value);
    }

    @Override
    public LargoValue mul(LargoValue rv) {
        return rv.mulWith(value);
    }

    @Override
    public LargoValue mulWith(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue mulWith(boolean lv) {
        return lv ? this : LargoInteger.ZERO;
    }

    @Override
    public LargoValue mulWith(double lv) {
        return LargoDouble.from(lv * value);
    }

    @Override
    public LargoValue mulWith(int lv) {
        return LargoDouble.from(lv * value);
    }

    @Override
    public LargoValue div(LargoValue rv) {
        return rv.divInto(value);
    }

    @Override
    public LargoValue divInto(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue divInto(boolean lv) {
        return lv ? LargoDouble.from(1.0 / value) : LargoInteger.ZERO;
    }

    @Override
    public LargoValue divInto(double lv) {
        return LargoDouble.from(lv / value);
    }

    @Override
    public LargoValue divInto(int lv) {
        return LargoDouble.from(lv / value);
    }

    @Override
    public LargoValue mod(LargoValue rv) {
        return rv.modFrom(value);
    }

    @Override
    public LargoValue modFrom(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue modFrom(boolean lv) {
        return value == 0 ? LargoDouble.NaN : LargoDouble.from((lv ? 1 : 0) % value);
    }

    @Override
    public LargoValue modFrom(double lv) {
        return LargoDouble.from(lv % value);
    }

    @Override
    public LargoValue modFrom(int lv) {
        return LargoDouble.from(lv % value);
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
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue eq(double rv) {
        return LargoBoolean.from(rv == value);
    }

    @Override
    public LargoValue eq(int rv) {
        return LargoBoolean.from(rv == value);
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
        return LargoBoolean.from(value < (rv ? 1 : 0));
    }

    @Override
    public LargoValue lt(double rv) {
        return LargoBoolean.from(value < rv);
    }

    @Override
    public LargoValue lt(int rv) {
        return LargoBoolean.from(value < rv);
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
        return LargoBoolean.from(value <= (rv ? 1 : 0));
    }

    @Override
    public LargoValue lteq(double rv) {
        return LargoBoolean.from(value <= rv);
    }

    @Override
    public LargoValue lteq(int rv) {
        return LargoBoolean.from(value <= rv);
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
        return LargoBoolean.from(value > (rv ? 1 : 0));
    }

    @Override
    public LargoValue gt(double rv) {
        return LargoBoolean.from(value > rv);
    }

    @Override
    public LargoValue gt(int rv) {
        return LargoBoolean.from(value > rv);
    }

    @Override
    public LargoValue gteq(LargoValue rv) {
        return rv.lteq(value);
    }

    @Override
    public LargoValue gteq(String rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gteq(boolean rv) {
        return LargoBoolean.from(value >= (rv ? 1 : 0));
    }

    @Override
    public LargoValue gteq(double rv) {
        return LargoBoolean.from(value >= rv);
    }

    @Override
    public LargoValue gteq(int rv) {
        return LargoBoolean.from(value >= rv);
    }
}
