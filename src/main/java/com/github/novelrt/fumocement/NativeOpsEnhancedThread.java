package com.github.novelrt.fumocement;

import com.github.novelrt.fumocement.memory.NativeStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A basic thread with FumoCement-related optimizations, which can be used instead
 * of the classic {@link Thread} class to speed up FumoCement operations.<br>
 * The class contains optimizations for:
 * <ul>
 *     <li>retrieving the current {@link NativeStack} with {@link NativeStack#current()}</li>
 * </ul>
 */
public class NativeOpsEnhancedThread extends Thread {
    private final NativeStack nativeStack = makeNativeStack();

    public NativeOpsEnhancedThread() {
    }

    public NativeOpsEnhancedThread(Runnable target) {
        super(target);
    }

    public NativeOpsEnhancedThread(@Nullable ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public NativeOpsEnhancedThread(@NotNull String name) {
        super(name);
    }

    public NativeOpsEnhancedThread(@Nullable ThreadGroup group, @NotNull String name) {
        super(group, name);
    }

    public NativeOpsEnhancedThread(Runnable target, String name) {
        super(target, name);
    }

    public NativeOpsEnhancedThread(@Nullable ThreadGroup group, Runnable target, @NotNull String name) {
        super(group, target, name);
    }

    public NativeOpsEnhancedThread(@Nullable ThreadGroup group, Runnable target, @NotNull String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    public NativeOpsEnhancedThread(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
        super(group, target, name, stackSize, inheritThreadLocals);
    }

    protected NativeStack makeNativeStack() {
        return new NativeStack();
    }

    public NativeStack getNativeStack() {
        return nativeStack;
    }

    @Override
    public void interrupt() {
        super.interrupt();
        nativeStack.close();
    }
}
