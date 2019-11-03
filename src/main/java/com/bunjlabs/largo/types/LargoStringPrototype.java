package com.bunjlabs.largo.types;

import java.util.regex.Pattern;

class LargoStringPrototype extends LargoPrototype {

    LargoStringPrototype() {
        set(LargoString.from("charAt"), LargoFunction.fromBiFunction(this::charAt));
        set(LargoString.from("codePointAt"), LargoFunction.fromBiFunction(this::codePointAt));
        set(LargoString.from("concat"), LargoFunction.fromBiFunction(this::concat));
        set(LargoString.from("contains"), LargoFunction.fromBiFunction(this::contains));
        set(LargoString.from("endWith"), LargoFunction.fromBiFunction(this::endWith));
        set(LargoString.from("hashCode"), LargoFunction.fromFunction(this::hashCode));
        set(LargoString.from("indexOf"), LargoFunction.fromVarArgFunction(this::indexOf));
        set(LargoString.from("isEmpty"), LargoFunction.fromFunction(this::isEmpty));
        set(LargoString.from("lastIndexOf"), LargoFunction.fromVarArgFunction(this::lastIndexOf));
        set(LargoString.from("length"), LargoFunction.fromFunction(this::length));
        set(LargoString.from("replace"), LargoFunction.fromVarArgFunction(this::replace));
        set(LargoString.from("replaceFirst"), LargoFunction.fromVarArgFunction(this::replaceFirst));
        set(LargoString.from("startWith"), LargoFunction.fromVarArgFunction(this::startWith));
        set(LargoString.from("substring"), LargoFunction.fromVarArgFunction(this::substring));
        set(LargoString.from("toLowerCase"), LargoFunction.fromFunction(this::toLowerCase));
        set(LargoString.from("toUpperCase"), LargoFunction.fromFunction(this::toUpperCase));
        set(LargoString.from("trim"), LargoFunction.fromFunction(this::trim));
    }

    private LargoValue charAt(LargoValue value, LargoValue index) {
        return LargoString.from(value.asJString().charAt(index.asJInteger()) + "");
    }

    private LargoValue codePointAt(LargoValue value, LargoValue index) {
        return LargoInteger.from(value.asJString().codePointAt(index.asJInteger()));
    }

    private LargoValue concat(LargoValue value, LargoValue str) {
        return LargoString.from(value.asJString().concat(str.asJString()));
    }

    private LargoValue contains(LargoValue value, LargoValue str) {
        return LargoBoolean.from(value.asJString().contains(str.asJString()));
    }

    private LargoValue endWith(LargoValue value, LargoValue str) {
        return LargoBoolean.from(value.asJString().endsWith(str.asJString()));
    }

    private LargoValue hashCode(LargoValue value) {
        return LargoInteger.from(value.asJString().hashCode());
    }

    private LargoValue indexOf(LargoValue value, LargoValue... args) {
        if (args.length == 0) {
            return LargoInteger.from(-1);
        } else if (args.length == 1) {
            return LargoInteger.from(value.asJString().indexOf(args[0].asJString()));
        } else {
            return LargoInteger.from(value.asJString().indexOf(args[0].asJString(), args[1].asJInteger()));
        }
    }

    private LargoValue isEmpty(LargoValue value) {
        return LargoBoolean.from(value.asJString().isEmpty());
    }

    private LargoValue lastIndexOf(LargoValue value, LargoValue... args) {
        if (args.length == 0) {
            return LargoInteger.from(-1);
        } else if (args.length == 1) {
            return LargoInteger.from(value.asJString().lastIndexOf(args[0].asJString()));
        } else {
            return LargoInteger.from(value.asJString().lastIndexOf(args[0].asJString(), args[1].asJInteger()));
        }
    }

    private LargoValue length(LargoValue ctx) {
        return LargoInteger.from(ctx.asJString().length());
    }

    private LargoValue replace(LargoValue value, LargoValue... args) {
        if (args.length == 0) {
            return value;
        } else if (args.length == 1) {
            Pattern p = Pattern.compile(args[0].asJString(), Pattern.LITERAL);
            return LargoString.from(p.matcher(args[0].asJString()).replaceAll(""));
        } else {
            Pattern p = Pattern.compile(args[0].asJString(), Pattern.LITERAL);
            return LargoString.from(p.matcher(args[0].asJString()).replaceAll(args[1].asJString()));
        }
    }

    private LargoValue replaceFirst(LargoValue value, LargoValue... args) {
        if (args.length == 0) {
            return value;
        } else if (args.length == 1) {
            Pattern p = Pattern.compile(args[0].asJString(), Pattern.LITERAL);
            return LargoString.from(p.matcher(args[0].asJString()).replaceFirst(""));
        } else {
            Pattern p = Pattern.compile(args[0].asJString(), Pattern.LITERAL);
            return LargoString.from(p.matcher(args[0].asJString()).replaceFirst(args[1].asJString()));
        }
    }

    private LargoValue startWith(LargoValue value, LargoValue... args) {
        if (args.length == 0) {
            return LargoBoolean.TRUE;
        } else if (args.length == 1) {
            return LargoBoolean.from(value.asJString().startsWith(args[0].asJString()));
        } else {
            return LargoBoolean.from(value.asJString().startsWith(args[0].asJString(), args[1].asJInteger()));
        }
    }

    private LargoValue substring(LargoValue value, LargoValue... args) {
        if (args.length == 0) {
            return value;
        } else if (args.length == 1) {
            return LargoString.from(value.asJString().substring(args[0].asJInteger()));
        } else {
            return LargoString.from(value.asJString().substring(args[0].asJInteger(), args[1].asJInteger()));
        }
    }

    private LargoValue toLowerCase(LargoValue value) {
        return LargoString.from(value.asJString().toLowerCase());
    }

    private LargoValue toUpperCase(LargoValue value) {
        return LargoString.from(value.asJString().toUpperCase());
    }

    private LargoValue trim(LargoValue value) {
        return LargoString.from(value.asJString().trim());
    }

}
