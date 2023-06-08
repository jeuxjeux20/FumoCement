// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.


package com.github.novelrt.fumocement.builtin;

import com.github.novelrt.fumocement.memory.NativeMemory;
import com.github.novelrt.fumocement.memory.NativeStack;

/**
 * Represents a {@code int8_t*} stored natively.
 */
public sealed class Int8Pointer {
    public static final long SIZE = 1;

    private final long address;

    public Int8Pointer(long address) {
        this.address = address;
    }

    public static StackAllocated allocate(NativeStack stack) {
        return new StackAllocated(stack.allocateManual(SIZE), stack);
    }

    public static Int8Pointer allocate(NativeStack.Scope scope) {
        return new Int8Pointer(scope.allocate(SIZE));
    }

    public byte getValue() {
        return NativeMemory.access().getByte(address);
    }

    public void setValue(byte value) {
        NativeMemory.access().putByte(address, value);
    }

    public long getAddress() {
        return address;
    }

    public static final class StackAllocated extends Int8Pointer implements AutoCloseable {
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
