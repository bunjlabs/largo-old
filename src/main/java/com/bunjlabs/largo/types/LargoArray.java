package com.bunjlabs.largo.types;

import com.bunjlabs.largo.LargoContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LargoArray extends LargoValue {
    private static final LargoPrototype PROTOTYPE = new Prototype();
    final List<LargoValue> list = new ArrayList<>();

    public static LargoArray empty() {
        return new LargoArray();
    }

    private LargoArray() {
    }

    @Override
    public String asJString() {
        return "[" + list.stream().map(LargoValue::asJString).collect(Collectors.joining(",")) + "]";
    }

    @Override
    public LargoArray asArray() {
        return this;
    }

    @Override
    public LargoObject asObject() {
        LargoObject object = LargoObject.empty();
        for (int i = 0; i < list.size(); i++) {
            object.set(LargoInteger.from(i), list.get(i));
        }
        return object;
    }

    @Override
    public LargoType getType() {
        return LargoType.ARRAY;
    }

    @Override
    public LargoPrototype getPrototype() {
        return PROTOTYPE;
    }

    @Override
    public LargoValue get(LargoValue key) {
        LargoValue value = key.getType() == LargoType.NUMBER ? list.get(key.asJInteger()) : null;
        return value != null ? value : super.get(key);
    }

    @Override
    public void set(LargoValue key, LargoValue value) {
        list.set(key.asJInteger(), value);
    }

    @Override
    public void push(LargoValue value) {
        list.add(value);
    }

    private static class Prototype extends LargoPrototype {

        Prototype() {
            setProperty("length", LargoFunction.fromFunction(this::length));
            setProperty("get", LargoFunction.fromBiFunction(this::getProperty));
            setProperty("set", LargoFunction.fromVarArgFunction(this::setProperty));
            setProperty("push", LargoFunction.fromBiFunction(this::push));
            setProperty("pop", LargoFunction.fromFunction(this::pop));
            setProperty("shift", LargoFunction.fromFunction(this::shift));
            setProperty("unshift", LargoFunction.fromVarArgFunction(this::unshift));
            setProperty("contains", LargoFunction.fromBiFunction(this::contains));
            setProperty("concat", LargoFunction.fromVarArgFunction(this::concat));
            setProperty("join", LargoFunction.fromBiFunction(this::join));
            setProperty("slice", LargoFunction.fromVarArgFunction(this::slice));
            setProperty("toString", LargoFunction.fromFunction(this::convertString));
            setProperty("indexOf", LargoFunction.fromBiFunction(this::indexOf));
            setProperty("lastIndexOf", LargoFunction.fromBiFunction(this::lastIndexOf));
            setProperty("forEach", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return forEach(context, thisRef, args);
                }
            });
            setProperty("every", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return every(context, thisRef, args);
                }
            });
            setProperty("some", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return some(context, thisRef, args);
                }
            });
            setProperty("filter", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return filter(context, thisRef, args);
                }
            });
            setProperty("find", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return find(context, thisRef, args);
                }
            });
            setProperty("findIndex", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return findIndex(context, thisRef, args);
                }
            });
            setProperty("map", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return mapf(context, thisRef, args);
                }
            });
            setProperty("reduce", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return reduce(context, thisRef, args);
                }
            });
            setProperty("reduceRight", new LargoFunction() {
                @Override
                public LargoValue call(LargoContext context, LargoValue thisRef, LargoValue... args) {
                    return reduceRight(context, thisRef, args);
                }
            });
        }

        private LargoValue length(LargoValue thisRef) {
            return LargoNumber.from(thisRef.asArray().list.size());
        }

        private LargoValue getProperty(LargoValue thisRef, LargoValue key) {
            return thisRef.get(key);
        }

        private LargoValue setProperty(LargoValue thisRef, LargoValue[] args) {
            if (args.length == 1) {
                thisRef.set(args[0], LargoUndefined.UNDEFINED);
            } else if (args.length > 1) {
                thisRef.set(args[0], args[1]);
            }
            return thisRef;
        }

        private LargoValue push(LargoValue thisRef, LargoValue value) {
            thisRef.asArray().list.add(value);
            return this;
        }

        private LargoValue pop(LargoValue thisRef) {
            List<LargoValue> list = thisRef.asArray().list;
            return list.remove(list.size() - 1);
        }

        private LargoValue shift(LargoValue thisRef) {
            return thisRef.asArray().list.remove(0);
        }

        private LargoValue unshift(LargoValue thisRef, LargoValue[] values) {
            LargoArray array = thisRef.asArray();
            for(LargoValue value : values) {
                array.list.add(0, value);
            }
            return array;
        }

        private LargoValue contains(LargoValue thisRef, LargoValue value) {
            return LargoBoolean.from(thisRef.asArray().list.contains(value));
        }

        private LargoValue concat(LargoValue thisRef, LargoValue[] values) {
            LargoArray array = LargoArray.empty();

            for (LargoValue value : values) {
                if (value.isArray()) {
                    array.list.addAll(value.asArray().list);
                } else {
                    array.list.add(value);
                }
            }

            return array;
        }

        private LargoValue join(LargoValue thisRef, LargoValue value) {
            StringBuilder sb = new StringBuilder();
            String delimiter = value.isUndefined() ? "" : value.asJString();
            for (LargoValue v : thisRef.asArray().list) {
                sb.append(v.asJString());
                sb.append(delimiter);
            }
            return LargoString.from(sb.toString());
        }

        private LargoValue slice(LargoValue thisRef, LargoValue[] args) {
            LargoArray array = LargoArray.empty();
            int begin = args.length > 0 ? args[0].asJInteger() : 0;
            int end = args.length > 1 ? args[1].asJInteger() : thisRef.asArray().list.size();
            int length = thisRef.asArray().list.size();

            array.list.addAll(thisRef.asArray().list.subList(mod(begin, length), mod(end, length)));
            return array;
        }

        private static int mod(int a, int b) {
            int ret = a % b;
            return ret < 0 ? ret + b : ret;
        }

        private LargoValue convertString(LargoValue thisRef) {
            return thisRef.asString();
        }

        private LargoValue indexOf(LargoValue thisRef, LargoValue value) {
            return LargoInteger.from(thisRef.asArray().list.indexOf(value));
        }

        private LargoValue lastIndexOf(LargoValue thisRef, LargoValue value) {
            return LargoInteger.from(thisRef.asArray().list.lastIndexOf(value));
        }

        private LargoValue forEach(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue innerThisRef = args.length > 1 ? args[1] : thisRef;
                for (int i = 0; i < array.list.size(); i++) {
                    args[0].call(context, innerThisRef, array.list.get(i), LargoInteger.from(i), array);
                }
            }
            return LargoUndefined.UNDEFINED;
        }

        private LargoValue every(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue innerThisRef = args.length > 1 ? args[1] : thisRef;
                for (int i = 0; i < array.list.size(); i++) {
                    if (!args[0].call(context, innerThisRef, array.list.get(i), LargoInteger.from(i), array).asJBoolean()) {
                        return LargoBoolean.FALSE;
                    }
                }
            }
            return LargoBoolean.TRUE;
        }

        private LargoValue some(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue innerThisRef = args.length > 1 ? args[1] : thisRef;
                for (int i = 0; i < array.list.size(); i++) {
                    if (args[0].call(context, innerThisRef, array.list.get(i), LargoInteger.from(i), array).asJBoolean()) {
                        return LargoBoolean.TRUE;
                    }
                }
            }
            return LargoBoolean.FALSE;
        }

        private LargoValue filter(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            LargoArray newArray = LargoArray.empty();
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue innerThisRef = args.length > 1 ? args[1] : thisRef;
                for (int i = 0; i < array.list.size(); i++) {
                    if (args[0].call(context, innerThisRef, array.list.get(i), LargoInteger.from(i), array).asJBoolean()) {
                        newArray.list.add(array.list.get(i));
                    }
                }
            }
            return newArray;
        }

        private LargoValue find(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue innerThisRef = args.length > 1 ? args[1] : thisRef;
                for (int i = 0; i < array.list.size(); i++) {
                    if (args[0].call(context, innerThisRef, array.list.get(i), LargoInteger.from(i), array).asJBoolean()) {
                        return array.list.get(i);
                    }
                }
            }
            return LargoUndefined.UNDEFINED;
        }

        private LargoValue findIndex(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue innerThisRef = args.length > 1 ? args[1] : thisRef;
                for (int i = 0; i < array.list.size(); i++) {
                    if (args[0].call(context, innerThisRef, array.list.get(i), LargoInteger.from(i), array).asJBoolean()) {
                        return LargoInteger.from(i);
                    }
                }
            }
            return LargoInteger.NEGATIVE_ONE;
        }

        private LargoValue mapf(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            LargoArray newArray = LargoArray.empty();
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue innerThisRef = args.length > 1 ? args[1] : thisRef;
                for (int i = 0; i < array.list.size(); i++) {
                    newArray.list.add(args[0].call(context, innerThisRef, array.list.get(i), LargoInteger.from(i), array));
                }
            }
            return newArray;
        }

        private LargoValue reduce(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue accumulator = args.length > 1 ? args[1] : array.list.get(0);
                for (int i = args.length > 1 ? 0 : 1; i < array.list.size(); i++) {
                    accumulator = args[0].call(context, thisRef, accumulator, array.list.get(i), LargoInteger.from(i), array);
                }
                return accumulator;
            }
            return LargoUndefined.UNDEFINED;
        }

        private LargoValue reduceRight(LargoContext context, LargoValue thisRef, LargoValue[] args) {
            if (args.length > 0) {
                LargoArray array = thisRef.asArray();
                LargoValue accumulator = args.length > 1 ? args[1] : array.list.get(0);
                int length = array.list.size();
                for (int i = args.length > 1 ? length : length - 1; i >= 0; i--) {
                    accumulator = args[0].call(context, thisRef, accumulator, array.list.get(i), LargoInteger.from(i), array);
                }
                return accumulator;
            }
            return LargoUndefined.UNDEFINED;
        }
    }
}
