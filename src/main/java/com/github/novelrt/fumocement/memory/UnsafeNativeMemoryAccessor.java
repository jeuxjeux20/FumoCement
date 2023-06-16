// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement.memory;

import com.github.novelrt.fumocement.Pointer;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeNativeMemoryAccessor implements NativeMemoryAccessor {
    private static final Unsafe unsafe;
    private static final boolean HAS_UNSAFE;

    static {
        Unsafe foundUnsafe = null;
        try {
            // This should also work on Android. I tested it on my phone, and it worked, so...
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            foundUnsafe = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            // No unsafe :(
        }
        unsafe = foundUnsafe;
        HAS_UNSAFE = unsafe != null;
    }

    @Override
    public ImplementationPriority getPriority() {
        return HAS_UNSAFE ? ImplementationPriority.NORMAL : ImplementationPriority.UNSUPPORTED;
    }

    @Override
    public boolean getBoolean(@Pointer long address) {
        return unsafe.getByte(address) != 0;
    }

    @Override
    public void putBoolean(@Pointer long address, boolean value) {
        unsafe.putByte(address, (byte) (value ? 1 : 0));
    }

    public byte getByte(@Pointer long address) {
        return unsafe.getByte(address);
    }

    public void putByte(@Pointer long address, byte x) {
        unsafe.putByte(address, x);
    }

    public short getShort(@Pointer long address) {
        return unsafe.getShort(address);
    }

    public void putShort(@Pointer long address, short x) {
        unsafe.putShort(address, x);
    }

    public char getChar(@Pointer long address) {
        return unsafe.getChar(address);
    }

    public void putChar(@Pointer long address, char x) {
        unsafe.putChar(address, x);
    }

    public int getInt(@Pointer long address) {
        return unsafe.getInt(address);
    }

    public void putInt(@Pointer long address, int x) {
        unsafe.putInt(address, x);
    }

    public long getLong(@Pointer long address) {
        return unsafe.getLong(address);
    }

    public void putLong(@Pointer long address, long x) {
        unsafe.putLong(address, x);
    }

    public float getFloat(@Pointer long address) {
        return unsafe.getFloat(address);
    }

    public void putFloat(@Pointer long address, float x) {
        unsafe.putFloat(address, x);
    }

    public double getDouble(@Pointer long address) {
        return unsafe.getDouble(address);
    }

    public void putDouble(@Pointer long address, double x) {
        unsafe.putDouble(address, x);
    }

    @Override
    public long getAddress(@Pointer long address) {
        return unsafe.getAddress(address);
    }

    @Override
    public void putAddress(@Pointer long address, long x) {
        unsafe.putAddress(address, x);
    }

    public @Pointer long allocateMemory(long bytes) {
        return unsafe.allocateMemory(bytes);
    }

    public void freeMemory(@Pointer long address) {
        unsafe.freeMemory(address);
    }

    public void copyMemory(long srcAddress, long destAddress, long bytes) {
        unsafe.copyMemory(srcAddress, destAddress, bytes);
    }

    @Override
    public void zeroMemory(long address, long bytes) {
        unsafe.setMemory(address, bytes, (byte) 0);
    }
}
