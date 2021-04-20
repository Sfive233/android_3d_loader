package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.dataType.Vector3;

public class SimpleMaterial extends Material{
    protected Vector3 color = new Vector3(0.5f, 0.5f, 0.5f);

    public SimpleMaterial(Context context) {
        super(context, R.raw.simple_vs, R.raw.simple_fs);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        shader.setVec3("Color", color);
    }

    public void setColor(float r, float g, float b){
        color.x.setVal(r);
        color.y.setVal(g);
        color.z.setVal(b);
    }
}
