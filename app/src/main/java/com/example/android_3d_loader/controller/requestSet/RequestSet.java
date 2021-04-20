package com.example.android_3d_loader.controller.requestSet;

import com.example.android_3d_loader.controller.GLRequest;
import com.example.android_3d_loader.controller.renderPass.RenderPass;

public class RequestSet {
    private GLRequest request;
    private RenderPass[] renderPasses;

    public RequestSet(GLRequest request, RenderPass... renderPasses){
        this.request = request;
        this.renderPasses = renderPasses;
    }

    public GLRequest getRequest() {
        return request;
    }

    public void setRequest(GLRequest request) {
        this.request = request;
    }

    public RenderPass[] getRenderPasses() {
        return renderPasses;
    }

    public void setRenderPasses(RenderPass[] renderPasses) {
        this.renderPasses = renderPasses;
    }
}
