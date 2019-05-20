package com.bunjlabs.largo.lib.functions;

import com.bunjlabs.largo.types.LargoUndefined;
import com.bunjlabs.largo.types.LargoValue;

import java.util.function.BiConsumer;

public class BiConsumerLargoFunction extends JavaLargoFunction {
    private final BiConsumer<LargoValue, LargoValue> consumer;

    public BiConsumerLargoFunction(BiConsumer<LargoValue, LargoValue> consumer) {
        this.consumer = consumer;
    }

    @Override
    public LargoValue call(LargoValue context, LargoValue... values) {
        consumer.accept(context, values.length > 0 ? values[0] : LargoUndefined.UNDEFINED);

        return LargoUndefined.UNDEFINED;
    }

}
