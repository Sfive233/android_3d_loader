package com.example.android_3d_loader.core.material;

import android.content.Context;

import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;

public class ScreenMaterial extends Material{

    private float[] color;
    private Texture2D texture = new Texture2D();
    private boolean isShowPureColor;
    private boolean isShowTexture;
    private boolean isGammaCorrection = true;

    public ScreenMaterial(Context context) {
        super(context, R.raw.screen_vs, R.raw.screen_fs);
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        shader.setBool("IsShowPureColor", isShowPureColor);
        shader.setBool("IsShowTexture", isShowTexture);
        if (isShowPureColor){
            shader.setVec3("Color", color);
        }
        if (isShowTexture){
            shader.setTexture("Texture", texture);
        }
        shader.setBool("IsGammaCorrection", isGammaCorrection);
    }

    public void setShowColor(float r, float g, float b) {
        isShowPureColor = true;
        isShowTexture = false;
        this.color = new float[]{r, g, b};
    }

    public void setShowTexture(Texture texture) {
        isShowTexture = true;
        isShowPureColor = false;
        this.texture.setTex(texture.getTex());
    }

    public void setGammaCorrection(boolean gammaCorrection) {
        isGammaCorrection = gammaCorrection;
    }
}
