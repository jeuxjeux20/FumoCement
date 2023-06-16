// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement.memory;

import com.github.novelrt.fumocement.Pointer;
import com.github.novelrt.fumocement.layout.NativeLayouts;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.ServiceLoader;

public interface NativeMemoryAccessor {
    @Nullable NativeMemoryAccessor INSTANCE = findImplementation();

    static NativeMemoryAccessor getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("There's no supported NativeMemoryAccessor.");
        }
        return INSTANCE;
    }

    private static NativeMemoryAccessor findImplementation() {
        return ServiceLoader.load(NativeMemoryAccessor.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .max(Comparator.comparing(NativeMemoryAccessor::getPriority)) // Highest prio
                .orElse(null);
    }

    ImplementationPriority getPriority();

    boolean getBoolean(@Pointer long address);

    void putBoolean(@Pointer long address, boolean value);

    byte getByte(@Pointer long address);

    void putByte(@Pointer long address, byte x);

    short getShort(@Pointer long address);

    void putShort(@Pointer long address, short x);

    char getChar(@Pointer long address);

    void putChar(@Pointer long address, char x);

    int getInt(@Pointer long address);

    void putInt(@Pointer long address, int x);

    long getLong(@Pointer long address);

    void putLong(@Pointer long address, long x);

    float getFloat(@Pointer long address);

    void putFloat(@Pointer long address, float x);

    double getDouble(@Pointer long address);

    void putDouble(@Pointer long address, double x);

    long getAddress(@Pointer long address);

    void putAddress(@Pointer long address, long x);


    default boolean getCBool(@Pointer long address) {
        return getBoolean(address);
    }

    default void putCBool(@Pointer long address, boolean value) {
        putBoolean(address, value);
    }

    default byte getCChar(@Pointer long address) {
        return getByte(address);
    }

    default void putCChar(@Pointer long address, byte value) {
        putByte(address, value);
    }

    default short getCShort(@Pointer long address) {
        return getShort(address);
    }

    default void putCShort(@Pointer long address, short value) {
        putShort(address, value);
    }

    default char getCUShort(@Pointer long address) {
        return getChar(address);
    }

    default void putCUShort(@Pointer long address, char value) {
        putChar(address, value);
    }

    default int getCInt(@Pointer long address) {
        return getInt(address);
    }

    default void putCInt(@Pointer long address, int value) {
        putInt(address, value);
    }

    default long getCLong(@Pointer long address) {
        class Constants {
            static final boolean useLong = NativeLayouts.CURRENT_PLATFORM.getLong().size() == 8;
        }
        if (Constants.useLong) {
            return getLong(address);
        } else {
            return getInt(address);
        }
    }

    default void putCLong(@Pointer long address, long value) {
        class Constants {
            static final boolean useLong = NativeLayouts.CURRENT_PLATFORM.getLong().size() == 8;
        }
        if (Constants.useLong) {
            putLong(address, value);
        } else {
            putInt(address, (int) value);
        }
    }

    default long getCLongLong(@Pointer long address) {
        return getLong(address);
    }

    default void putCLongLong(@Pointer long address, long value) {
        putLong(address, value);
    }

    default float getCFloat(@Pointer long address) {
        return getFloat(address);
    }

    default void putCFloat(@Pointer long address, float value) {
        putFloat(address, value);
    }

    default double getCDouble(@Pointer long address) {
        return getDouble(address);
    }

    default void putCDouble(@Pointer long address, double value) {
        putDouble(address, value);
    }

    @Pointer long allocateMemory(long bytes);

    void freeMemory(@Pointer long address);

    void copyMemory(@Pointer long srcAddress, @Pointer long destAddress, long bytes);

    void zeroMemory(long address, long bytes);

    enum ImplementationPriority {
        UNSUPPORTED,
        NORMAL,
        PREFERRED,
        USER_OVERRIDE
    }
}
