// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement.memory;

import com.github.novelrt.fumocement.Pointer;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Provides tools to manipulate native memory.
 */
public final class NativeMemory {

    /**
     * Provides a {@link NativeMemoryAccessor} to access off-heap memory.
     *
     * @return an instance of {@link NativeMemoryAccessor}
     */
    public static NativeMemoryAccessor access() {
        return NativeMemoryAccessor.getInstance();
    }
}
