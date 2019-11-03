package com.bunjlabs.largo.types;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class LargoString extends LargoValue {
    public static final LargoString EMPTY = new LargoString("");

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.ROOT);
    private static final LargoPrototype prototype = new LargoStringPrototype();
    private final String value;

    private LargoString(String value) {
        this.value = value;
    }

    public static LargoString from(String s) {
        return new LargoString(s);
    }

    @Override
    public LargoType getType() {
        return LargoType.STRING;
    }

    public LargoPrototype getPrototype() {
        return LargoPrototypes.STRING;
    }

    @Override
    public String asJString() {
        return value;
    }

    @Override
    public double asJDouble() {
        try {
            return NUMBER_FORMAT.parse(value).doubleValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    @Override
    public int asJInteger() {
        try {
            return NUMBER_FORMAT.parse(value).intValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    @Override
    public boolean asJBoolean() {
        return !value.isEmpty();
    }

    @Override
    public Object asJObject() {
        return value;
    }

    @Override
    public LargoString asString() {
        return this;
    }

    @Override
    public LargoNumber asNumber() {
        return LargoDouble.from(asJDouble());
    }

    @Override
    public LargoBoolean asBoolean() {
        return asJBoolean() ? LargoBoolean.TRUE : LargoBoolean.FALSE;
    }

    @Override
    public LargoValue not() {
        return asBoolean().not();
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
    public LargoValue addTo(String lv) {
        return LargoString.from(lv + value);
    }

    @Override
    public LargoValue addTo(boolean lv) {
        return LargoString.from(lv + value);
    }

    @Override
    public LargoValue addTo(double lv) {
        return LargoString.from(lv + value);
    }

    @Override
    public LargoValue addTo(int lv) {
        return LargoString.from(lv + value);
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
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue subFrom(double lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue subFrom(int lv) {
        return LargoDouble.NaN;
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
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue mulWith(double lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue mulWith(int lv) {
        return LargoDouble.NaN;
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
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue divInto(double lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue divInto(int lv) {
        return LargoDouble.NaN;
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
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue modFrom(double lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue modFrom(int lv) {
        return LargoDouble.NaN;
    }

    @Override
    public LargoValue eq(LargoValue rv) {
        return rv.eq(value);
    }

    @Override
    public LargoValue eq(String rv) {
        return LargoBoolean.from(rv.equals(value));
    }

    @Override
    public LargoValue eq(boolean rv) {
        return LargoBoolean.FALSE;
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
        return LargoBoolean.from(rv.compareTo(value) > 0);
    }

    @Override
    public LargoValue lt(boolean rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue lt(double rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue lt(int rv) {
        return LargoBoolean.FALSE;
    }


    @Override
    public LargoValue lteq(LargoValue rv) {
        return rv.gteq(value);
    }

    @Override
    public LargoValue lteq(String rv) {
        return LargoBoolean.from(rv.compareTo(value) >= 0);
    }

    @Override
    public LargoValue lteq(boolean rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue lteq(double rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue lteq(int rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gt(LargoValue rv) {
        return rv.lt(value);
    }

    @Override
    public LargoValue gt(String rv) {
        return LargoBoolean.from(rv.compareTo(value) < 0);
    }

    @Override
    public LargoValue gt(boolean rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gt(double rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gt(int rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gteq(LargoValue rv) {
        return rv.lteq(value);
    }

    @Override
    public LargoValue gteq(String rv) {
        return LargoBoolean.from(rv.compareTo(value) <= 0);
    }

    @Override
    public LargoValue gteq(boolean rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gteq(double rv) {
        return LargoBoolean.FALSE;
    }

    @Override
    public LargoValue gteq(int rv) {
        return LargoBoolean.FALSE;
    }


}
