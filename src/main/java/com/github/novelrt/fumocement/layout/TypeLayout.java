// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement.layout;

/**
 * Represents a native type layout in memory, with its size and alignment.
 */
public record TypeLayout(long size, long alignment) {
    public static TypeLayout JAVA_BOOLEAN = naturallyAligned(1);
    public static TypeLayout JAVA_BYTE = naturallyAligned(1);
    public static TypeLayout JAVA_CHAR = naturallyAligned(2);
    public static TypeLayout JAVA_SHORT = naturallyAligned(2);
    public static TypeLayout JAVA_INT = naturallyAligned(4);
    public static TypeLayout JAVA_LONG = naturallyAligned(8);
    public static TypeLayout JAVA_FLOAT = naturallyAligned(4);
    public static TypeLayout JAVA_DOUBLE = naturallyAligned(8);

    public static TypeLayout naturallyAligned(long size) {
        return new TypeLayout(size, size);
    }

    public TypeLayout {
        if (size < 1) {
            throw new IllegalArgumentException("Cannot create a type layout with zero or negative size.");
        }
        if (alignment < 1) {
            throw new IllegalArgumentException("Cannot create a type layout with zero or negative alignment.");
        }
        if (size < alignment) {
            throw new IllegalArgumentException("Size cannot be smaller than alignment.");
        }
    }
}
