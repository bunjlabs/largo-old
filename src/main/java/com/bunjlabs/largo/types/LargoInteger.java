package com.bunjlabs.largo.types;

public class LargoInteger extends LargoNumber {
    public static final LargoInteger MAX_VALUE = new LargoInteger(Integer.MAX_VALUE);
    public static final LargoInteger MIN_VALUE = new LargoInteger(Integer.MIN_VALUE);
    public static final LargoInteger ZERO = new LargoInteger(0);
    public static final LargoInteger ONE = new LargoInteger(1);
    public static final LargoInteger NEGATIVE_ONE = new LargoInteger(-1);

    private static final LargoInteger[] intValues = new LargoInteger[512];

    static {
        for (int i = 0; i < 512; i++) intValues[i] = new LargoInteger(i - 256);
    }

    private final int value;

    private LargoInteger(int value) {
        this.value = value;
    }

    public static LargoInteger from(int value) {
        return value <= 255 && value >= -256 ? intValues[value + 256] : new LargoInteger(value);
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
        return value;
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
        return LargoInteger.from(-value);
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
    LargoValue addTo(String lv) {
        return LargoString.from(lv + value);
    }

    @Override
    LargoValue addTo(boolean lv) {
        return lv ? LargoInteger.from(1 + value) : this;
    }

    @Override
    LargoValue addTo(double lv) {
        return LargoDouble.from(lv + value);
    }

    @Override
    LargoValue addTo(int lv) {
        return LargoInteger.from(lv + value);
    }

    @Override
    public LargoValue sub(LargoValue rv) {
        return rv.subFrom(value);
    }

    @Override
    LargoValue subFrom(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    LargoValue subFrom(boolean lv) {
        return LargoInteger.from((lv ? 1 : 0) - value);
    }

    @Override
    LargoValue subFrom(double lv) {
        return LargoDouble.from(lv - value);
    }

    @Override
    LargoValue subFrom(int lv) {
        return LargoInteger.from(lv - value);
    }

    @Override
    public LargoValue mul(LargoValue rv) {
        return rv.mulWith(value);
    }

    @Override
    LargoValue mulWith(String lv) {
        return LargoDouble.NaN;
    }

    @Override
    LargoValue mulWith(boolean lv) {
        return lv ? this : LargoInteger.ZERO;
    }

    @Override
    LargoValue mulWith(double lv) {
        return LargoDouble.from(lv * value);
    }

    @Override
    LargoValue mulWith(int lv) {
        return LargoInteger.from(lv * value);
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
        return lv ? LargoDouble.from(1.0 / value) : LargoInteger.ZERO;
    }

    @Override
    LargoValue divInto(double lv) {
        return LargoDouble.from(lv / value);
    }

    @Override
    LargoValue divInto(int lv) {
        return LargoDouble.from(lv / value);
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
        return value == 0 ? LargoDouble.NaN : LargoInteger.from((lv ? 1 : 0) % value);
    }

    @Override
    LargoValue modFrom(double lv) {
        return LargoDouble.from(lv % value);
    }

    @Override
    LargoValue modFrom(int lv) {
        return LargoInteger.from(lv % value);
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
