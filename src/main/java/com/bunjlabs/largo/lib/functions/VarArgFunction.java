package com.bunjlabs.largo.lib.functions;

@FunctionalInterface
public interface VarArgFunction<U, V, R> {

    R apply(U value, V... args);
}