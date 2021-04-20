package com.example.android_3d_loader.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_3d_loader.R;
import com.example.android_3d_loader.core.util.ImageLoader;
import com.example.android_3d_loader.view.property.PropertyDefinition;

import java.io.IOException;
import java.io.Serializable;

public class WidgetTexture extends LinearLayout implements Serializable {

    private final TextView mTextureName;
    private final TextView mTexturePath;
    private ImageView mTextureImage;

    private final PropertyDefinition mPropertyDefinition;

    private static WidgetTexture lastClickedWidgetTexture;

    public WidgetTexture(Context context, PropertyDefinition propertyDefinition) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_texture, this);
        mPropertyDefinition = propertyDefinition;
        mTextureName = findViewById(R.id.texture_name);
        mTexturePath = findViewById(R.id.texture_path);

        initTexturePreview();
    }

    public void onTextureClickListener(OnClickListener l){
        mTextureImage.setOnClickListener(l);
    }

    public void initTexturePreview(){
        mTextureName.setText(mPropertyDefinition.getName());

        mTexturePath.setText(mPropertyDefinition.getBindTexture().getPath());

        mTextureImage = findViewById(R.id.texture_image);
        try {
            Bitmap bitmap = ImageLoader.getBitmap(getContext(), mPropertyDefinition.getBindTexture().getPath());
            if(bitmap == null){

            }else {
                Glide.with(getContext())
                        .load(ImageLoader.getBytesFromBitmap(bitmap))
                        .into(mTextureImage);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public PropertyDefinition getPropertyDefinition() {
        return mPropertyDefinition;
    }

    public static WidgetTexture getLastClickedWidgetTexture() {
        return lastClickedWidgetTexture;
    }

    public static void setLastClickedWidgetTexture(WidgetTexture lastClickedWidgetTexture) {
        WidgetTexture.lastClickedWidgetTexture = lastClickedWidgetTexture;
    }
}
