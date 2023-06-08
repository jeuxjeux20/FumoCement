// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.


package com.github.novelrt.fumocement.builtin;

import com.github.novelrt.fumocement.memory.NativeMemory;
import com.github.novelrt.fumocement.memory.NativeStack;

/**
 * Represents a {@code int32_t*} stored natively.
 */
public sealed class Int32Pointer {
    public static final long SIZE = 4;

    private final long address;

    public Int32Pointer(long address) {
        this.address = address;
    }

    public static StackAllocated allocate(NativeStack stack) {
        return new StackAllocated(stack.allocateManual(SIZE), stack);
    }

    public static Int32Pointer allocate(NativeStack.Scope scope) {
        return new Int32Pointer(scope.allocate(SIZE));
    }

    public int getValue() {
        return NativeMemory.access().getInt(address);
    }

    public void setValue(int value) {
        NativeMemory.access().putInt(address, value);
    }

    public long getAddress() {
        return address;
    }

    public static final class StackAllocated extends Int32Pointer implements AutoCloseable {
        private final NativeStack stack;

        private StackAllocated(long address, NativeStack stack) {
            super(address);
            this.stack = stack;
        }

        @Override
        public void close() {
            stack.freeManual(SIZE);
        }
    }
}
