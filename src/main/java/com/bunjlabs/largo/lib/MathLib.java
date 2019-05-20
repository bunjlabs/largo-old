package com.bunjlabs.largo.lib;

import com.bunjlabs.largo.lib.functions.LibFunctions;
import com.bunjlabs.largo.types.LargoDouble;
import com.bunjlabs.largo.types.LargoInteger;
import com.bunjlabs.largo.types.LargoString;
import com.bunjlabs.largo.types.LargoValue;

public class MathLib extends LargoLibrary {

    public static final MathLib MATH = new MathLib();

    private MathLib() {
        set(LargoString.from("E"), LargoDouble.from(Math.E));
        set(LargoString.from("PI"), LargoDouble.from(Math.PI));

        set(LargoString.from("sin"), LibFunctions.biFunction(this::sin));
        set(LargoString.from("cos"), LibFunctions.biFunction(this::cos));
        set(LargoString.from("tan"), LibFunctions.biFunction(this::tan));
        set(LargoString.from("asin"), LibFunctions.biFunction(this::asin));
        set(LargoString.from("acos"), LibFunctions.biFunction(this::acos));
        set(LargoString.from("atan"), LibFunctions.biFunction(this::atan));
        set(LargoString.from("toRadians"), LibFunctions.biFunction(this::toRadians));
        set(LargoString.from("toDegrees"), LibFunctions.biFunction(this::toDegrees));
        set(LargoString.from("exp"), LibFunctions.biFunction(this::exp));
        set(LargoString.from("log"), LibFunctions.biFunction(this::log));
        set(LargoString.from("log10"), LibFunctions.biFunction(this::log10));
        set(LargoString.from("sqrt"), LibFunctions.biFunction(this::sqrt));
        set(LargoString.from("cbrt"), LibFunctions.biFunction(this::cbrt));
        set(LargoString.from("ceil"), LibFunctions.biFunction(this::ceil));
        set(LargoString.from("floor"), LibFunctions.biFunction(this::floor));
        set(LargoString.from("rint"), LibFunctions.biFunction(this::rint));
        set(LargoString.from("atan2"), LibFunctions.varArgFunction(this::atan2));
        set(LargoString.from("pow"), LibFunctions.varArgFunction(this::pow));
        set(LargoString.from("round"), LibFunctions.biFunction(this::round));
        set(LargoString.from("random"), LibFunctions.function(this::random));
        set(LargoString.from("abs"), LibFunctions.biFunction(this::abs));
        set(LargoString.from("max"), LibFunctions.varArgFunction(this::max));
        set(LargoString.from("min"), LibFunctions.varArgFunction(this::min));
        set(LargoString.from("sinh"), LibFunctions.biFunction(this::sinh));
        set(LargoString.from("cosh"), LibFunctions.biFunction(this::cosh));
        set(LargoString.from("tanh"), LibFunctions.biFunction(this::tanh));
        set(LargoString.from("hypot"), LibFunctions.varArgFunction(this::hypot));
        set(LargoString.from("expm1"), LibFunctions.biFunction(this::expm1));
        set(LargoString.from("log1p"), LibFunctions.biFunction(this::log1p));
        set(LargoString.from("getExponent"), LibFunctions.biFunction(this::getExponent));
    }

    private LargoValue sin(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.sin(a.asJDouble()));
    }

    private LargoValue cos(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.cos(a.asJDouble()));
    }

    private LargoValue tan(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.tan(a.asJDouble()));
    }

    private LargoValue asin(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.asin(a.asJDouble()));
    }

    private LargoValue acos(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.acos(a.asJDouble()));
    }

    private LargoValue atan(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.atan(a.asJDouble()));
    }

    private LargoValue toRadians(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.toRadians(a.asJDouble()));
    }

    private LargoValue toDegrees(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.toDegrees(a.asJDouble()));
    }

    private LargoValue exp(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.exp(a.asJDouble()));
    }

    private LargoValue log(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.log(a.asJDouble()));
    }

    private LargoValue log10(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.log10(a.asJDouble()));
    }

    private LargoValue sqrt(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.sqrt(a.asJDouble()));
    }

    private LargoValue cbrt(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.cbrt(a.asJDouble()));
    }

    private LargoValue ceil(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.ceil(a.asJDouble()));
    }

    private LargoValue floor(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.floor(a.asJDouble()));
    }

    private LargoValue rint(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.rint(a.asJDouble()));
    }

    private LargoValue atan2(LargoValue ctx, LargoValue[] args) {
        return LargoDouble.from(Math.atan2(args[0].asJDouble(), args[1].asJDouble()));
    }

    private LargoValue pow(LargoValue ctx, LargoValue[] args) {
        return LargoDouble.from(Math.pow(args[0].asJDouble(), args[1].asJDouble()));
    }

    private LargoValue round(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.round(a.asJDouble()));
    }

    private LargoValue random(LargoValue ctx) {
        return LargoDouble.from(Math.random());
    }

    private LargoValue abs(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.abs(a.asJDouble()));
    }

    private LargoValue max(LargoValue ctx, LargoValue[] args) {
        return LargoDouble.from(Math.max(args[0].asJDouble(), args[1].asJDouble()));
    }

    private LargoValue min(LargoValue ctx, LargoValue[] args) {
        return LargoDouble.from(Math.min(args[0].asJDouble(), args[1].asJDouble()));
    }

    private LargoValue sinh(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.sinh(a.asJDouble()));
    }

    private LargoValue cosh(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.cosh(a.asJDouble()));
    }

    private LargoValue tanh(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.tanh(a.asJDouble()));
    }

    private LargoValue hypot(LargoValue ctx, LargoValue[] args) {
        return LargoDouble.from(Math.hypot(args[0].asJDouble(), args[1].asJDouble()));
    }

    private LargoValue expm1(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.expm1(a.asJDouble()));
    }

    private LargoValue log1p(LargoValue ctx, LargoValue a) {
        return LargoDouble.from(Math.log1p(a.asJDouble()));
    }

    private LargoValue getExponent(LargoValue ctx, LargoValue a) {
        return LargoInteger.from(Math.getExponent(a.asJDouble()));
    }

}
