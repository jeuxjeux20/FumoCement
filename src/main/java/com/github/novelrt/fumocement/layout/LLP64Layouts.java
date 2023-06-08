// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement.layout;

public final class LLP64Layouts implements NativeLayouts {
    @Override
    public Compatibility evaluateCompatibility(String architecture, String operatingSystem) {
        if ((architecture.equals("amd64") ||
             architecture.equals("x86_64") ||
             architecture.equals("aarch64")) &&
            !operatingSystem.startsWith("Windows")) {
            return Compatibility.COMPATIBLE;
        } else {
            return Compatibility.INADEQUATE;
        }
    }

    @Override
    public TypeLayout getShort() {
        return TypeLayout.naturallyAligned(2);
    }

    @Override
    public TypeLayout getInt() {
        return TypeLayout.naturallyAligned(4);
    }

    @Override
    public TypeLayout getLong() {
        return TypeLayout.naturallyAligned(4);
    }

    @Override
    public TypeLayout getLongLong() {
        return TypeLayout.naturallyAligned(8);
    }

    @Override
    public TypeLayout getPointer() {
        return TypeLayout.naturallyAligned(8);
    }
}
