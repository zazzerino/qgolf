package com.kdp.golf.lib;

import java.util.function.*;

/**
 * Adapted from an example by Vivek Jaiswal.
 * https://github.com/liquidpie/lenses-java
 */
public class Lens<A, B> {

    private final Function<A, B> getter;
    private final BiFunction<A, B, A> setter;

    public Lens(Function<A, B> getter, BiFunction<A, B, A> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public static <A, B> Lens<A, B> of(Function<A, B> getter,
                                       BiFunction<A, B, A> setter) {
        return new Lens<>(getter, setter);
    }

    public B get(A a) {
        return getter.apply(a);
    }

    public A set(A a, B b) {
        return setter.apply(a, b);
    }

    public A mod(A a, UnaryOperator<B> operator) {
        return set(a, operator.apply(get(a)));
    }

    public <C> Lens<C, B> compose(Lens<C, A> lens) {
        return new Lens<>(
                c -> get(lens.get(c)),
                (c, b) -> lens.mod(c, a -> set(a, b))
        );
    }

    public <C> Lens<A, C> andThen(Lens<B, C> lens) {
        return lens.compose(this);
    }
}
