package com.github.novelrt.fumocement;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE_USE, ElementType.LOCAL_VARIABLE})
@Documented
public @interface ReturnValueOutput {
    String value();
}
