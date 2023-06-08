// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

package com.github.novelrt.fumocement.layout;

/**
 * Constructs a struct layout using the most common struct alignment rules.
 */
public class StructLayoutArranger {
    private final NativeLayouts nativeLayouts;
    private long structAlignment = 1;
    private long structSize;
    private boolean complete = false;

    public StructLayoutArranger() {
        this(NativeLayouts.CURRENT_PLATFORM);
    }

    public StructLayoutArranger(NativeLayouts nativeLayouts) {
        this.nativeLayouts = nativeLayouts;
    }

    public long addCBoolField() {
        return addField(nativeLayouts.getBool());
    }

    public long addCCharField() {
        return addField(nativeLayouts.getChar());
    }

    public long addCShortField() {
        return addField(nativeLayouts.getShort());
    }

    public long addCIntField() {
        return addField(nativeLayouts.getInt());
    }

    public long addCLongField() {
        return addField(nativeLayouts.getLong());
    }

    public long addCLongLongField() {
        return addField(nativeLayouts.getLongLong());
    }

    public long addCFloatField() {
        return addField(nativeLayouts.getFloat());
    }

    public long addCDoubleField() {
        return addField(nativeLayouts.getDouble());
    }

    public long addCPointerField() {
        return addField(nativeLayouts.getPointer());
    }

    public long addFixedField(long size) {
        return addField(TypeLayout.naturallyAligned(size));
    }

    public long addField(TypeLayout layout) {
        if (complete) {
            throw new IllegalStateException("Cannot add a field to a completed struct.");
        }
        if (structAlignment < layout.alignment()) {
            structAlignment = layout.alignment();
        }
        long alignmentPadding = findAlignmentPadding(structSize, layout.alignment());
        long location = structSize + alignmentPadding;
        structSize += alignmentPadding + layout.size();
        return location;
    }

    public TypeLayout completeStruct() {
        if (!complete) {
            structSize += findAlignmentPadding(structSize, structAlignment); // Final padding
            complete = true;
        }
        return new TypeLayout(structSize, structAlignment);
    }

    private long findAlignmentPadding(long containerSize, long alignment) {
        // The last modulo ensures that when (containerSize % alignment) == 0, the result becomes 0.
        return (alignment - (containerSize % alignment)) % alignment;
    }

    public boolean isComplete() {
        return complete;
    }

    public long getStructSize() {
        return structSize;
    }

    public long getStructAlignment() {
        return structAlignment;
    }
}
