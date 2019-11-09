package com.bunjlabs.largo.types;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

public class LargoString extends LargoValue {
    private static final LargoPrototype PROTOTYPE = new Prototype();

    private static final LargoString EMPTY = new LargoString("");

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.ROOT);
    private final String value;

    public static LargoString empty() {
        return EMPTY;
    }

    public static LargoString from(String s) {
        return new LargoString(s);
    }

    private LargoString(String value) {
        this.value = value;
    }


    @Override
    public LargoType getType() {
        return LargoType.STRING;
    }

    @Override
    public LargoPrototype getPrototype() {
        return PROTOTYPE;
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

    private static class Prototype extends LargoPrototype {

        Prototype() {
            setProperty("charAt", LargoFunction.fromBiFunction(this::charAt));
            setProperty("codePointAt", LargoFunction.fromBiFunction(this::codePointAt));
            setProperty("concat", LargoFunction.fromBiFunction(this::concat));
            setProperty("contains", LargoFunction.fromBiFunction(this::contains));
            setProperty("endWith", LargoFunction.fromBiFunction(this::endWith));
            setProperty("hashCode", LargoFunction.fromFunction(this::hashCode));
            setProperty("indexOf", LargoFunction.fromVarArgFunction(this::indexOf));
            setProperty("isEmpty", LargoFunction.fromFunction(this::isEmpty));
            setProperty("lastIndexOf", LargoFunction.fromVarArgFunction(this::lastIndexOf));
            setProperty("length", LargoFunction.fromFunction(this::length));
            setProperty("replace", LargoFunction.fromVarArgFunction(this::replace));
            setProperty("replaceFirst", LargoFunction.fromVarArgFunction(this::replaceFirst));
            setProperty("startWith", LargoFunction.fromVarArgFunction(this::startWith));
            setProperty("substring", LargoFunction.fromVarArgFunction(this::substring));
            setProperty("toLowerCase", LargoFunction.fromFunction(this::toLowerCase));
            setProperty("toUpperCase", LargoFunction.fromFunction(this::toUpperCase));
            setProperty("trim", LargoFunction.fromFunction(this::trim));
            setProperty("valueOf", LargoFunction.fromBiFunction(this::valueOf));
            setProperty("toString", LargoFunction.fromFunction(this::convertString));
        }

        private LargoValue charAt(LargoValue thisRef, LargoValue index) {
            return LargoString.from(thisRef.asJString().charAt(index.asJInteger()) + "");
        }

        private LargoValue codePointAt(LargoValue thisRef, LargoValue index) {
            return LargoInteger.from(thisRef.asJString().codePointAt(index.asJInteger()));
        }

        private LargoValue concat(LargoValue thisRef, LargoValue str) {
            return LargoString.from(thisRef.asJString().concat(str.asJString()));
        }

        private LargoValue contains(LargoValue thisRef, LargoValue str) {
            return LargoBoolean.from(thisRef.asJString().contains(str.asJString()));
        }

        private LargoValue endWith(LargoValue thisRef, LargoValue str) {
            return LargoBoolean.from(thisRef.asJString().endsWith(str.asJString()));
        }

        private LargoValue hashCode(LargoValue thisRef) {
            return LargoInteger.from(thisRef.asJString().hashCode());
        }

        private LargoValue indexOf(LargoValue thisRef, LargoValue... args) {
            if (args.length == 0) {
                return LargoInteger.from(-1);
            } else if (args.length == 1) {
                return LargoInteger.from(thisRef.asJString().indexOf(args[0].asJString()));
            } else {
                return LargoInteger.from(thisRef.asJString().indexOf(args[0].asJString(), args[1].asJInteger()));
            }
        }

        private LargoValue isEmpty(LargoValue thisRef) {
            return LargoBoolean.from(thisRef.asJString().isEmpty());
        }

        private LargoValue lastIndexOf(LargoValue thisRef, LargoValue... args) {
            if (args.length == 0) {
                return LargoInteger.from(-1);
            } else if (args.length == 1) {
                return LargoInteger.from(thisRef.asJString().lastIndexOf(args[0].asJString()));
            } else {
                return LargoInteger.from(thisRef.asJString().lastIndexOf(args[0].asJString(), args[1].asJInteger()));
            }
        }

        private LargoValue length(LargoValue thisRef) {
            return LargoInteger.from(thisRef.asJString().length());
        }

        private LargoValue replace(LargoValue thisRef, LargoValue... args) {
            if (args.length == 0) {
                return thisRef;
            } else if (args.length == 1) {
                Pattern p = Pattern.compile(args[0].asJString(), Pattern.LITERAL);
                return LargoString.from(p.matcher(args[0].asJString()).replaceAll(""));
            } else {
                Pattern p = Pattern.compile(args[0].asJString(), Pattern.LITERAL);
                return LargoString.from(p.matcher(args[0].asJString()).replaceAll(args[1].asJString()));
            }
        }

        private LargoValue replaceFirst(LargoValue thisRef, LargoValue... args) {
            if (args.length == 0) {
                return thisRef;
            } else if (args.length == 1) {
                Pattern p = Pattern.compile(args[0].asJString(), Pattern.LITERAL);
                return LargoString.from(p.matcher(args[0].asJString()).replaceFirst(""));
            } else {
                Pattern p = Pattern.compile(args[0].asJString(), Pattern.LITERAL);
                return LargoString.from(p.matcher(args[0].asJString()).replaceFirst(args[1].asJString()));
            }
        }

        private LargoValue startWith(LargoValue thisRef, LargoValue... args) {
            if (args.length == 0) {
                return LargoBoolean.TRUE;
            } else if (args.length == 1) {
                return LargoBoolean.from(thisRef.asJString().startsWith(args[0].asJString()));
            } else {
                return LargoBoolean.from(thisRef.asJString().startsWith(args[0].asJString(), args[1].asJInteger()));
            }
        }

        private LargoValue substring(LargoValue thisRef, LargoValue... args) {
            if (args.length == 0) {
                return thisRef;
            } else if (args.length == 1) {
                return LargoString.from(thisRef.asJString().substring(args[0].asJInteger()));
            } else {
                return LargoString.from(thisRef.asJString().substring(args[0].asJInteger(), args[1].asJInteger()));
            }
        }

        private LargoValue toLowerCase(LargoValue thisRef) {
            return LargoString.from(thisRef.asJString().toLowerCase());
        }

        private LargoValue toUpperCase(LargoValue thisRef) {
            return LargoString.from(thisRef.asJString().toUpperCase());
        }

        private LargoValue trim(LargoValue thisRef) {
            return LargoString.from(thisRef.asJString().trim());
        }

        private LargoValue valueOf(LargoValue thisRef, LargoValue value) {
            return value.asString();
        }

        private LargoValue convertString(LargoValue thisRef) {
            return thisRef.asString();
        }
    }
}
