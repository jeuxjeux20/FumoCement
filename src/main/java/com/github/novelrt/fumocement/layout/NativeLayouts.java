// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement.layout;

import com.github.novelrt.fumocement.memory.NativeMemoryAccessor;

import java.util.Comparator;
import java.util.ServiceLoader;

/**
 * Contains all C type layouts for a platform.
 */
public interface NativeLayouts {
    NativeLayouts CURRENT_PLATFORM = findCurrent();

    private static NativeLayouts findCurrent() {
        // Should we even support 32 bit? Modern JVMs don't even support 32 bit.

        String modelOverride = System.getProperty("novelrt.fumocement.nativeDataModel");
        if (modelOverride != null) {
            if (modelOverride.equalsIgnoreCase("LLP64")) {
                return new LLP64Layouts();
            } else if (modelOverride.equalsIgnoreCase("LP64")) {
                return new LP64Layouts();
            } else {
                throw new UnsupportedOperationException("Unknown data model: " + modelOverride);
            }
        }

        // svm.targetArch is the target architecture of a GraalVM Native Image build.
        String arch = System.getProperty("svm.targetArch", System.getProperty("os.arch"));
        String os = System.getProperty("os.name");

        return ServiceLoader.load(NativeLayouts.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .max(Comparator.comparing(x -> x.evaluateCompatibility(arch, os))) // Highest prio
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported platform: " + os + " on " + arch));
    }

    Compatibility evaluateCompatibility(String architecture, String operatingSystem);

    default TypeLayout getBool() {
        return TypeLayout.naturallyAligned(1);
    }

    default TypeLayout getChar() {
        return TypeLayout.naturallyAligned(1);
    }

    TypeLayout getShort();

    TypeLayout getInt();

    TypeLayout getLong();

    TypeLayout getLongLong();

    default TypeLayout getFloat() {
        return TypeLayout.naturallyAligned(4);
    }

    default TypeLayout getDouble() {
        return TypeLayout.naturallyAligned(8);
    }

    TypeLayout getPointer();

    enum Compatibility {
        INADEQUATE,
        COMPATIBLE,
        USER_OVERRIDE
    }
}
