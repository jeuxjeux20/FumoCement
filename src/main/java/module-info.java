// Copyright © Matt Jones and Contributors. Licensed under the MIT License (MIT). See LICENCE.md in the repository root for more information.

/**
 * The main FumoCement module.
 */
module novelrt.fumocement {
    requires static org.jetbrains.annotations;
    requires jdk.unsupported;

    exports com.github.novelrt.fumocement;
    exports com.github.novelrt.fumocement.builtin;
    exports com.github.novelrt.fumocement.layout;
    exports com.github.novelrt.fumocement.memory;

    uses com.github.novelrt.fumocement.memory.NativeMemoryAccessor;
    uses com.github.novelrt.fumocement.layout.NativeLayouts;
    provides com.github.novelrt.fumocement.memory.NativeMemoryAccessor with
            com.github.novelrt.fumocement.memory.UnsafeNativeMemoryAccessor;

    provides com.github.novelrt.fumocement.layout.NativeLayouts with
            com.github.novelrt.fumocement.layout.LP64Layouts,
            com.github.novelrt.fumocement.layout.LLP64Layouts;
}
