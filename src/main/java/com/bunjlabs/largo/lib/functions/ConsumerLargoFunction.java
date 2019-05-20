package com.bunjlabs.largo.lib.functions;

import com.bunjlabs.largo.types.LargoUndefined;
import com.bunjlabs.largo.types.LargoValue;

import java.util.function.Consumer;

public class ConsumerLargoFunction extends JavaLargoFunction {
    private final Consumer<LargoValue> consumer;

    public ConsumerLargoFunction(Consumer<LargoValue> consumer) {
        this.consumer = consumer;
    }

    @Override
    public LargoValue call(LargoValue context, LargoValue... values) {
        consumer.accept(context);

        return LargoUndefined.UNDEFINED;
    }
}
