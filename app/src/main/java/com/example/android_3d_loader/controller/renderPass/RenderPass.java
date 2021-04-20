package com.example.android_3d_loader.controller.renderPass;

import android.content.Context;

public interface RenderPass {
    void renderInit();
    void renderReact();
    Object[] renderUpdate(Object... inputs);
    void setContext(Context context);
}
