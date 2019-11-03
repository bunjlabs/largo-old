package com.bunjlabs.largo.lib;

import com.bunjlabs.largo.types.*;

public class MathLib extends LargoLibrary {

    public static final MathLib LIB = new MathLib();

    private MathLib() {
        set(LargoString.from("E"), LargoDouble.from(Math.E));
        set(LargoString.from("PI"), LargoDouble.from(Math.PI));

        set(LargoString.from("sin"), LargoFunction.fromBiFunction(this::sin));
        set(LargoString.from("cos"), LargoFunction.fromBiFunction(this::cos));
        set(LargoString.from("tan"), LargoFunction.fromBiFunction(this::tan));
        set(LargoString.from("asin"), LargoFunction.fromBiFunction(this::asin));
        set(LargoString.from("acos"), LargoFunction.fromBiFunction(this::acos));
        set(LargoString.from("atan"), LargoFunction.fromBiFunction(this::atan));
        set(LargoString.from("toRadians"), LargoFunction.fromBiFunction(this::toRadians));
        set(LargoString.from("toDegrees"), LargoFunction.fromBiFunction(this::toDegrees));
        set(LargoString.from("exp"), LargoFunction.fromBiFunction(this::exp));
        set(LargoString.from("log"), LargoFunction.fromBiFunction(this::log));
        set(LargoString.from("log10"), LargoFunction.fromBiFunction(this::log10));
        set(LargoString.from("sqrt"), LargoFunction.fromBiFunction(this::sqrt));
        set(LargoString.from("cbrt"), LargoFunction.fromBiFunction(this::cbrt));
        set(LargoString.from("ceil"), LargoFunction.fromBiFunction(this::ceil));
        set(LargoString.from("floor"), LargoFunction.fromBiFunction(this::floor));
        set(LargoString.from("rint"), LargoFunction.fromBiFunction(this::rint));
        set(LargoString.from("atan2"), LargoFunction.fromVarArgFunction(this::atan2));
        set(LargoString.from("pow"), LargoFunction.fromVarArgFunction(this::pow));
        set(LargoString.from("round"), LargoFunction.fromBiFunction(this::round));
        set(LargoString.from("random"), LargoFunction.fromFunction(this::random));
        set(LargoString.from("abs"), LargoFunction.fromBiFunction(this::abs));
        set(LargoString.from("max"), LargoFunction.fromVarArgFunction(this::max));
        set(LargoString.from("min"), LargoFunction.fromVarArgFunction(this::min));
        set(LargoString.from("sinh"), LargoFunction.fromBiFunction(this::sinh));
        set(LargoString.from("cosh"), LargoFunction.fromBiFunction(this::cosh));
        set(LargoString.from("tanh"), LargoFunction.fromBiFunction(this::tanh));
        set(LargoString.from("hypot"), LargoFunction.fromVarArgFunction(this::hypot));
        set(LargoString.from("expm1"), LargoFunction.fromBiFunction(this::expm1));
        set(LargoString.from("log1p"), LargoFunction.fromBiFunction(this::log1p));
        set(LargoString.from("getExponent"), LargoFunction.fromBiFunction(this::getExponent));
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
