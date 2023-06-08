// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.


package com.github.novelrt.fumocement.builtin;

import com.github.novelrt.fumocement.memory.NativeMemory;
import com.github.novelrt.fumocement.memory.NativeStack;

/**
 * Represents a {@code int16_t*} stored natively.
 */
public sealed class Int16Pointer {
    public static final long SIZE = 2;

    private final long address;

    public Int16Pointer(long address) {
        this.address = address;
    }

    public static StackAllocated allocate(NativeStack stack) {
        return new StackAllocated(stack.allocateManual(SIZE), stack);
    }

    public static Int16Pointer allocate(NativeStack.Scope scope) {
        return new Int16Pointer(scope.allocate(SIZE));
    }

    public short getValue() {
        return NativeMemory.access().getShort(address);
    }

    public void setValue(short value) {
        NativeMemory.access().putShort(address, value);
    }

    public long getAddress() {
        return address;
    }

    public static final class StackAllocated extends Int16Pointer implements AutoCloseable {
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
