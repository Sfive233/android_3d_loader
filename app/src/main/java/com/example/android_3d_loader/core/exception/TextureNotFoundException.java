package com.example.android_3d_loader.core.exception;

import java.io.FileNotFoundException;

public class TextureNotFoundException extends AssetsNotFoundException {
    public TextureNotFoundException() {
    }

    public TextureNotFoundException(String message) {
        super(message);
    }
}
