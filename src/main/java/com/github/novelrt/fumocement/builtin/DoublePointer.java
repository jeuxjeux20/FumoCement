// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.


package com.github.novelrt.fumocement.builtin;

import com.github.novelrt.fumocement.memory.NativeMemory;
import com.github.novelrt.fumocement.memory.NativeStack;

/**
 * Represents a {@code double*} stored natively.
 */
public sealed class DoublePointer {
    public static final long SIZE = 8;

    private final long address;

    public DoublePointer(long address) {
        this.address = address;
    }

    public static StackAllocated allocate(NativeStack stack) {
        return new StackAllocated(stack.allocateManual(SIZE), stack);
    }

    public static DoublePointer allocate(NativeStack.Scope scope) {
        return new DoublePointer(scope.allocate(SIZE));
    }

    public double getValue() {
        return NativeMemory.access().getDouble(address);
    }

    public void setValue(double value) {
        NativeMemory.access().putDouble(address, value);
    }

    public long getAddress() {
        return address;
    }

    public static final class StackAllocated extends DoublePointer implements AutoCloseable {
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
