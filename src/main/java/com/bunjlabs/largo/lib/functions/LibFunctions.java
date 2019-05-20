package com.bunjlabs.largo.lib.functions;

import com.bunjlabs.largo.types.LargoFunction;
import com.bunjlabs.largo.types.LargoValue;

import java.util.function.*;

public abstract class LibFunctions {

    public static LargoFunction consumer(Consumer<LargoValue> consumer) {
        return new ConsumerLargoFunction(consumer);
    }

    public static LargoFunction supplier(Supplier<LargoValue> supplier) {
        return new SupplierLargoFunction(supplier);
    }

    public static LargoFunction biConsumer(BiConsumer<LargoValue, LargoValue> consumer) {
        return new BiConsumerLargoFunction(consumer);
    }

    public static LargoFunction function(Function<LargoValue, LargoValue> function) {
        return new FunctionLargoFunction(function);
    }

    public static LargoFunction biFunction(BiFunction<LargoValue, LargoValue, LargoValue> function) {
        return new BiFunctionLargoFunction(function);
    }

    public static LargoFunction varArgFunction(VarArgFunction<LargoValue, LargoValue, LargoValue> function) {
        return new VarArgFunctionLargoFunction(function);
    }
}
