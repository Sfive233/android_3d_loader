package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.dataType.Vector3;

public class WireFrameMaterial extends Material{

    protected Vector3 color = new Vector3(1.0f);

    public WireFrameMaterial(Context context) {
        super(context, R.raw.wire_frame_vs, R.raw.wire_frame_fs);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        shader.setVec3("Color", color);
    }

    public Vector3 getColor() {
        return color;
    }
}
