// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement;

import com.github.novelrt.fumocement.builtin.Int32Pointer;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents an indirected pointer of type {@code T}, which can be represented in C
 * as {@code T*}. For instance, putting a {@link Int32Pointer} will result in a
 * {@code int**} type.
 * <p>
 * This class uses a {@link UnownedNativeObjectProvider} to provide native objects
 * that consumes the underlying handle without owning it.
 *
 * @param <T> the native object type that this double pointer contains
 * @implNote Under the hood, this class allocates a {@code void**}.
 */
public final class IndirectedPointer<T extends NativeObject> extends NativeObject {
    private final UnownedNativeObjectProvider<T> provider;

    private @Pointer("T*") long lastUnderlyingHandle;
    private @Nullable T lastUnderlyingHandleAsObject;

    /**
     * Creates a new instance of {@link IndirectedPointer} with the given {@link UnownedNativeObjectProvider}
     * which is used for giving native objects that serves as an access layer for the underlying
     * pointer. This object's native resources will be garbage collected.
     *
     * @param provider the native object provider to use
     * @throws NullPointerException when {@code provider} is null
     */
    public IndirectedPointer(UnownedNativeObjectProvider<T> provider) {
        this(provider, DisposalMethod.GARBAGE_COLLECTED);
    }

    /**
     * Creates a new instance of {@link IndirectedPointer} with the given {@link UnownedNativeObjectProvider}
     * which is used for giving native objects that serves as an access layer for the underlying
     * pointer, and with the given {@link DisposalMethod}.
     *
     * @param provider the native object provider to use
     * @param disposalMethod the disposal method to use
     * @throws NullPointerException when {@code provider} is null
     */
    public IndirectedPointer(UnownedNativeObjectProvider<T> provider, DisposalMethod disposalMethod) {
        super(createPointer(), true, disposalMethod, IndirectedPointer::destroyPointer);
        this.provider = Objects.requireNonNull(provider);
    }

    // For now, these are implemented in the methods generated by ClangSharp
    // fork. Maybe that we can do something like a separate C++ library,
    // but this will induce much more work as one has to create a CMake project,
    // make sure it works on Windows, Linux, Mac, Android, etc. Then, you
    // must somehow package the dll/so/dylib in the jar and also somehow load
    // it, as it must be loaded from a file. So yeah, it's a pain.

    private static native long getNativeUnderlyingHandle(long handle);

    private static native long setNativeUnderlyingHandle(long handle, long value);

    private static native long createPointer();

    private static native void destroyPointer(long handle);

    /**
     * {@inheritDoc}
     */
    @Override
    public @Pointer("void**") long getHandle() {
        return super.getHandle();
    }

    /**
     * Gets the underlying value of this double pointer as an instance of {@code T}.
     *
     * @return an instance of {@code T} from the underlying value of this pointer,
     * or {@code null} if the pointer is null
     */
    public @Nullable T get() {
        long underlyingHandle = getNativeUnderlyingHandle(getHandle());
        if (lastUnderlyingHandle != underlyingHandle) {
            lastUnderlyingHandle = underlyingHandle;

            if (Pointers.isNullPointer(underlyingHandle)) {
                lastUnderlyingHandleAsObject = null;
            } else {
                lastUnderlyingHandleAsObject = provider.provide(underlyingHandle);
            }
        }

        return lastUnderlyingHandleAsObject;
    }

    /**
     * Gets the underlying handle of this double pointer, as a pointer such as {@code T*}.
     *
     * @return the underlying handle
     */
    public @Pointer("T*") long getUnderlyingHandle() {
        return getNativeUnderlyingHandle(getHandle());
    }

    /**
     * Sets the underlying value of this double pointer using the given instance of @{code T}.
     * <p>
     * A value of {@code null} will result in a null value inside the double pointer.
     *
     * @param value the new value
     */
    public void set(@Nullable T value) {
        setNativeUnderlyingHandle(getHandle(), value == null ? Pointers.NULLPTR : value.getHandle());
    }

    /**
     * Sets the underlying value of this double pointer to {@code null}.
     */
    public void setNull() {
        setNativeUnderlyingHandle(getHandle(), Pointers.NULLPTR);
    }
}
