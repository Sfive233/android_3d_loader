package com.example.android_3d_loader.core.exception;

import java.io.IOException;

public class AssetsNotFoundException extends Exception {
    public AssetsNotFoundException() {
    }

    public AssetsNotFoundException(String message) {
        super(message);
    }
}
