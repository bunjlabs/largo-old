package com.bunjlabs.largo.lib;

import com.bunjlabs.largo.types.LargoFunction;
import com.bunjlabs.largo.types.LargoString;
import com.bunjlabs.largo.types.LargoUndefined;
import com.bunjlabs.largo.types.LargoValue;

import java.util.Arrays;

public class StdIoLib extends LargoLibrary {
    public static final StdIoLib LIB = new StdIoLib();

    private StdIoLib() {
        set(LargoString.from("printf"), LargoFunction.fromVarArgFunction(this::printf));
        set(LargoString.from("print"), LargoFunction.fromBiFunction(this::print));
        set(LargoString.from("println"), LargoFunction.fromBiFunction(this::println));
    }

    private LargoValue println(LargoValue context, LargoValue arg) {
        System.out.println(arg.asJString());
        return LargoUndefined.UNDEFINED;
    }

    private LargoValue print(LargoValue context, LargoValue arg) {
        System.out.print(arg.asJString());
        return LargoUndefined.UNDEFINED;
    }

    private LargoValue printf(LargoValue context, LargoValue[] args) {
        if (args.length > 1) {
            LargoValue[] largoArgs = Arrays.copyOfRange(args, 1, args.length);
            Object[] javaArgs = new Object[largoArgs.length];
            for (int i = 0; i < largoArgs.length; i++) {
                javaArgs[i] = largoArgs[i].asJObject();
            }

            System.out.printf(args[0].asJString(), javaArgs);
        }
        return LargoUndefined.UNDEFINED;
    }

}
