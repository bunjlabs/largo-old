package com.bunjlabs.largo.types;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class LargoFunction extends LargoValue {

    public static LargoFunction fromFunction(Function<LargoValue, LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoValue context, LargoValue[] args) {
                return function.apply(context);
            }
        };
    }

    public static LargoFunction fromVarArgFunction(BiFunction<LargoValue, LargoValue[], LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoValue context, LargoValue[] args) {
                return function.apply(context, args);
            }
        };
    }

    public static LargoFunction fromBiFunction(BiFunction<LargoValue, LargoValue, LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoValue context, LargoValue[] args) {
                return function.apply(context, args.length > 0 ? args[0] : LargoUndefined.UNDEFINED);
            }
        };
    }

    public static LargoFunction fromConsumer(Consumer<LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoValue context, LargoValue[] args) {
                function.accept(context);
                return LargoUndefined.UNDEFINED;
            }
        };
    }

    public static LargoFunction fromBiConsumer(BiConsumer<LargoValue, LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoValue context, LargoValue[] args) {
                function.accept(context, args.length > 0 ? args[0] : LargoUndefined.UNDEFINED);
                return LargoUndefined.UNDEFINED;
            }
        };
    }

    public static LargoFunction fromVarArgConsumer(BiConsumer<LargoValue, LargoValue[]> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoValue context, LargoValue[] args) {
                function.accept(context, args);
                return LargoUndefined.UNDEFINED;
            }
        };
    }

    public LargoType getType() {
        return LargoType.FUNCTION;
    }

    public LargoFunction bind(LargoValue context) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoValue context, LargoValue... args) {
                return LargoFunction.this.call(context, args);
            }
        };
    }
}
