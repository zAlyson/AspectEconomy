package com.alysonsantos.aspect.database;

import java.util.function.Function;

/**
 * A function capable of processing exceptions
 *
 * @param <T> Type to process
 * @param <R> Type to return
 * @author zkingboos
 */
public interface ExceptionFunction<T, R> extends Function<T, R> {

    @Override
    default R apply(T t) {
        try {
            return kApply(t);
        } catch (Exception e) {
            return null;
        }
    }

    R kApply(T t) throws Exception;

}
