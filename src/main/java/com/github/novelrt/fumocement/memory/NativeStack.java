// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement.memory;

import com.github.novelrt.fumocement.NativeObject;
import com.github.novelrt.fumocement.NativeOpsEnhancedThread;

/**
 * A First-In-Last-Out (FILO) buffer using native memory,
 * simulating a stack in assembly (like the RSP register in x86).
 * <p>
 * The stack is meant as a way to allocate method-scoped memory,
 * mainly for passing structs and arrays to native methods. It can as well be used
 * to receive struct return values.
 * <p>
 * The default size of a stack is, by default, of 64 KB per thread. This can be changed
 * with the {@code novelrt.fumocement.stack.size} system property.
 */
public class NativeStack extends NativeObject {
    public static final long DEFAULT_SIZE;

    static {
        String stackSize = System.getProperty("novelrt.fumocement.stack.size");
        if (stackSize != null) {
            DEFAULT_SIZE = validateSize(Long.parseLong(stackSize));
        } else {
            DEFAULT_SIZE = 64 * 1024;
        }
    }

    private static final ThreadLocal<NativeStack> currentTL = ThreadLocal.withInitial(NativeStack::new);

    public static NativeStack current() {
        Thread thread = Thread.currentThread();
        if (thread.getUncaughtExceptionHandler() instanceof WrappingUEH stack) {
            return stack;
        } else if (thread instanceof NativeOpsEnhancedThread enhancedThread) {
            return enhancedThread.getNativeStack();
        } else {
            return currentTL.get();
        }
    }

    public static void optimizeCurrentThread() {
        optimizeThread(Thread.currentThread());
    }

    public static void optimizeThread(Thread thread) {
        Thread.UncaughtExceptionHandler handler = thread.getUncaughtExceptionHandler();
        if (handler instanceof WrappingUEH) {
            return;
        }

        var stack = new WrappingUEH(NativeStack.DEFAULT_SIZE, handler);
        thread.setUncaughtExceptionHandler(stack);

        if (thread == Thread.currentThread()) {
            // Clear the old stack if we can.
            currentTL.remove();
        }
    }

    private static long validateSize(long size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid stack size: " + size);
        }
        return size;
    }

    private long stackPointer;
    private final long size;
    private final long startingAddress;

    public NativeStack() {
        this(DEFAULT_SIZE);
    }

    public NativeStack(long size) {
        super(NativeMemory.access().allocateMemory(validateSize(size)), true, NativeMemory.access()::freeMemory);
        // Imitate a stack going downwards
        startingAddress = getHandle() + size;
        stackPointer = startingAddress;
        this.size = size;
    }

    public final Resource allocate(long size) {
        stackPointer -= size;
        checkOverflow(size);
        return new Resource(stackPointer, size);
    }

    public final long allocateManual(long size) {
        stackPointer -= size;
        checkOverflow(size);
        return stackPointer;
    }

    public final void freeManual(long size) {
        stackPointer += size;
    }

    public final Scope scope() {
        return new Scope();
    }

    public final long getSize() {
        return size;
    }

    public final long getStartingAddress() {
        return startingAddress;
    }

    private void checkOverflow(long allocSize) {
        assert stackPointer >= getHandleUnsafe() :
                "Stack overflow while allocating " + allocSize + " bytes. " +
                "The stack size is " + getSize() + " bytes.\n" +
                "There can be multiple reasons as to why this happened:\n" +
                "   - You may have allocated too much memory in a very large stack scope.\n" +
                "     Consider splitting your code into smaller scopes.\n\n" +
                "   - You may have forgotten to free up memory using the allocate/free methods.\n\n" +
                "   - The stack is simply too small for the amount of memory you're allocating.\n" +
                "     Consider increasing the stack size using the novelrt.fumocement.stack.size system property.\n";
    }

    public final class Scope implements AutoCloseable {
        private long offset;

        private Scope() {
        }

        public long allocate(long size) {
            stackPointer -= size;
            offset += size;

            checkOverflow(size);
            return stackPointer;
        }

        public NativeStack getStack() {
            return NativeStack.this;
        }

        @Override
        public void close() {
            stackPointer += offset;
        }
    }

    public final class Resource implements AutoCloseable {
        private final long address;
        private final long size;

        private Resource(long address, long size) {
            this.address = address;
            this.size = size;
        }

        @Override
        public void close() {
            stackPointer += size;
        }

        public long address() {
            return address;
        }

        public long size() {
            return size;
        }
    }

    private static final class WrappingUEH extends NativeStack implements Thread.UncaughtExceptionHandler {
        private final Thread.UncaughtExceptionHandler ueh;


        public WrappingUEH(long size, Thread.UncaughtExceptionHandler ueh) {
            super(size);
            this.ueh = ueh;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            if (ueh != null) {
                ueh.uncaughtException(t, e);
            }
        }
    }
}

