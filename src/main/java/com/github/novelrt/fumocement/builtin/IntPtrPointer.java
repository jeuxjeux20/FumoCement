// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.


package com.github.novelrt.fumocement.builtin;

import com.github.novelrt.fumocement.layout.NativeLayouts;
import com.github.novelrt.fumocement.memory.NativeMemory;
import com.github.novelrt.fumocement.memory.NativeStack;

/**
 * Represents a {@code intptr_t*} stored natively.
 */
public sealed class IntPtrPointer {
    public static final long SIZE = NativeLayouts.CURRENT_PLATFORM.getPointer().size();

    private final long address;

    public IntPtrPointer(long address) {
        this.address = address;
    }

    public static StackAllocated allocate(NativeStack stack) {
        return new StackAllocated(stack.allocateManual(SIZE), stack);
    }

    public static IntPtrPointer allocate(NativeStack.Scope scope) {
        return new IntPtrPointer(scope.allocate(SIZE));
    }

    public long getValue() {
        return NativeMemory.access().getAddress(address);
    }

    public void setValue(long value) {
        NativeMemory.access().putAddress(address, value);
    }

    public long getAddress() {
        return address;
    }

    public static final class StackAllocated extends IntPtrPointer implements AutoCloseable {
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
