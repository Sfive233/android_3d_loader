package com.example.android_3d_loader.controller;

public enum GLRequest {
    BOOT,
    RUNNING,

    BACK_FROM_BACKGROUND,
    BACK_FROM_SWAPPING_TEXTURE,
    BACK_FROM_SWAPPING_MODEL,
    BACK_FROM_SWAPPING_HDRI,

    SWITCH_TONE_MAPPING,
    SWITCH_SHADER
}
