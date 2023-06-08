// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.


package com.github.novelrt.fumocement.builtin;

import com.github.novelrt.fumocement.memory.NativeMemory;
import com.github.novelrt.fumocement.memory.NativeStack;

/**
 * Represents a {@code int64_t*} stored natively.
 */
public sealed class Int64Pointer {
    public static final long SIZE = 8;

    private final long address;

    public Int64Pointer(long address) {
        this.address = address;
    }

    public static StackAllocated allocate(NativeStack stack) {
        return new StackAllocated(stack.allocateManual(SIZE), stack);
    }

    public static Int64Pointer allocate(NativeStack.Scope scope) {
        return new Int64Pointer(scope.allocate(SIZE));
    }

    public long getValue() {
        return NativeMemory.access().getLong(address);
    }

    public void setValue(long value) {
        NativeMemory.access().putLong(address, value);
    }

    public long getAddress() {
        return address;
    }

    public static final class StackAllocated extends Int64Pointer implements AutoCloseable {
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
