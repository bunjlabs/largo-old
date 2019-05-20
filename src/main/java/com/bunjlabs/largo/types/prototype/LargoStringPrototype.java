package com.bunjlabs.largo.types.prototype;

import com.bunjlabs.largo.lib.functions.LibFunctions;
import com.bunjlabs.largo.types.LargoBoolean;
import com.bunjlabs.largo.types.LargoInteger;
import com.bunjlabs.largo.types.LargoString;
import com.bunjlabs.largo.types.LargoValue;

import java.util.regex.Pattern;

public class LargoStringPrototype extends LargoPrototype {

    public LargoStringPrototype() {
        set(LargoString.from("charAt"), LibFunctions.biFunction(this::charAt));
        set(LargoString.from("codePointAt"), LibFunctions.biFunction(this::codePointAt));
        set(LargoString.from("concat"), LibFunctions.biFunction(this::concat));
        set(LargoString.from("contains"), LibFunctions.biFunction(this::contains));
        set(LargoString.from("endWith"), LibFunctions.biFunction(this::endWith));
        set(LargoString.from("hashCode"), LibFunctions.function(this::hashCode));
        set(LargoString.from("indexOf"), LibFunctions.varArgFunction(this::indexOf));
        set(LargoString.from("isEmpty"), LibFunctions.function(this::isEmpty));
        set(LargoString.from("lastIndexOf"), LibFunctions.varArgFunction(this::lastIndexOf));
        set(LargoString.from("length"), LibFunctions.function(this::length));
        set(LargoString.from("replace"), LibFunctions.varArgFunction(this::replace));
        set(LargoString.from("replaceFirst"), LibFunctions.varArgFunction(this::replaceFirst));
        set(LargoString.from("startWith"), LibFunctions.varArgFunction(this::startWith));
        set(LargoString.from("substring"), LibFunctions.varArgFunction(this::substring));
        set(LargoString.from("toLowerCase"), LibFunctions.function(this::toLowerCase));
        set(LargoString.from("toUpperCase"), LibFunctions.function(this::toUpperCase));
        set(LargoString.from("trim"), LibFunctions.function(this::trim));
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
