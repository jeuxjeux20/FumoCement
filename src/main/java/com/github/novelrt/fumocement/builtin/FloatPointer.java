// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.


package com.github.novelrt.fumocement.builtin;

import com.github.novelrt.fumocement.memory.NativeMemory;
import com.github.novelrt.fumocement.memory.NativeStack;

/**
 * Represents a {@code float*} stored natively.
 */
public sealed class FloatPointer {
    public static final long SIZE = 4;

    private final long address;

    public FloatPointer(long address) {
        this.address = address;
    }

    public static StackAllocated allocate(NativeStack stack) {
        return new StackAllocated(stack.allocateManual(SIZE), stack);
    }

    public static FloatPointer allocate(NativeStack.Scope scope) {
        return new FloatPointer(scope.allocate(SIZE));
    }

    public float getValue() {
        return NativeMemory.access().getFloat(address);
    }

    public void setValue(float value) {
        NativeMemory.access().putFloat(address, value);
    }

    public long getAddress() {
        return address;
    }

    public static final class StackAllocated extends FloatPointer implements AutoCloseable {
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
