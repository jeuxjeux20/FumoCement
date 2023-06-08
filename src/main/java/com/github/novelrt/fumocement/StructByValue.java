package com.github.novelrt.fumocement;

import java.lang.annotation.*;

/**
 * Declares that the parameter passes a struct by value, using a pointer
 * to the struct represented by a {@code long}.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE_USE, ElementType.LOCAL_VARIABLE})
@Documented
public @interface StructByValue {
    String value();
}
