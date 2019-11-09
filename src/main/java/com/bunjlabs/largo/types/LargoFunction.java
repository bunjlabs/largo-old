package com.bunjlabs.largo.types;

import com.bunjlabs.largo.LargoContext;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class LargoFunction extends LargoValue {
    private static final LargoPrototype PROTOTYPE = new Prototype();
    private static final LargoFunction EMPTY = new LargoFunction() {
        @Override
        public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
            return LargoUndefined.UNDEFINED;
        }
    };

    public static LargoFunction empty() {
        return EMPTY;
    }

    public static LargoFunction fromFunction(Function<LargoValue, LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue[] args) {
                return function.apply(thisRef);
            }
        };
    }

    public static LargoFunction fromVarArgFunction(BiFunction<LargoValue, LargoValue[], LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue[] args) {
                return function.apply(thisRef, args);
            }
        };
    }

    public static LargoFunction fromBiFunction(BiFunction<LargoValue, LargoValue, LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue[] args) {
                return function.apply(thisRef, args.length > 0 ? args[0] : LargoUndefined.UNDEFINED);
            }
        };
    }

    public static LargoFunction fromConsumer(Consumer<LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue[] args) {
                function.accept(thisRef);
                return LargoUndefined.UNDEFINED;
            }
        };
    }

    public static LargoFunction fromBiConsumer(BiConsumer<LargoValue, LargoValue> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue[] args) {
                function.accept(thisRef, args.length > 0 ? args[0] : LargoUndefined.UNDEFINED);
                return LargoUndefined.UNDEFINED;
            }
        };
    }

    public static LargoFunction fromVarArgConsumer(BiConsumer<LargoValue, LargoValue[]> function) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue[] args) {
                function.accept(thisRef, args);
                return LargoUndefined.UNDEFINED;
            }
        };
    }

    @Override
    public LargoFunction asFunction() {
        return this;
    }

    @Override
    public LargoType getType() {
        return LargoType.FUNCTION;
    }

    @Override
    public LargoPrototype getPrototype() {
        return PROTOTYPE;
    }

    public LargoFunction bind(LargoValue thisRef, LargoValue... firstArgs) {
        return new LargoFunction() {
            @Override
            public LargoValue call(LargoContext context, LargoValue ignore, LargoValue... lastArgs) {
                if (firstArgs.length > 0 && lastArgs.length > 0) {
                    LargoValue[] args = new LargoValue[firstArgs.length + lastArgs.length];
                    System.arraycopy(firstArgs, 0, args, 0, firstArgs.length);
                    System.arraycopy(lastArgs, 0, args, firstArgs.length, lastArgs.length);
                    return LargoFunction.this.call(context, thisRef, args);
                } else if (firstArgs.length > 0) {
                    return LargoFunction.this.call(context, thisRef, firstArgs);
                } else if (lastArgs.length > 0) {
                    return LargoFunction.this.call(context, thisRef, lastArgs);
                } else {
                    return LargoFunction.this.call(context, thisRef);
                }
            }
        };
    }

    private static class Prototype extends LargoPrototype {

        Prototype() {
            setProperty("apply", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return apply(context, thisRef, args);
                }
            });
            setProperty("call", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return callf(context, thisRef, args);
                }
            });
            setProperty("bind", LargoFunction.fromVarArgFunction(this::bind));
            setProperty("toString", LargoFunction.fromFunction(this::convertString));
        }


        private LargoValue apply(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoValue innerThisRef = args.length > 1 ? args[1] : thisRef;
                LargoArray innerArgs = args.length > 2 ? args[2].asArray() : LargoArray.empty();
                thisRef.call(context, innerThisRef, innerArgs.list.toArray(new LargoValue[]{}));
            }
            return LargoUndefined.UNDEFINED;
        }

        private LargoValue callf(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoValue innerThisRef = args[0];
                if (args.length > 1) {
                    return thisRef.call(context, innerThisRef, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    return thisRef.call(context, innerThisRef);
                }
            }
            return thisRef.call(context, thisRef);
        }


        private LargoValue bind(LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoValue innerThisRef = args[0];
                if (args.length > 1) {
                    return thisRef.asFunction().bind(innerThisRef, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    return thisRef.asFunction().bind(innerThisRef);
                }
            }
            return thisRef;
        }


        private LargoValue convertString(LargoValue thisRef) {
            return thisRef.asString();
        }
    }
}
