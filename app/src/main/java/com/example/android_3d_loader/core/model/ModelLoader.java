package com.example.android_3d_loader.core.model;

import android.content.Context;
import android.widget.Toast;

import com.example.android_3d_loader.core.EngineConstants;
import com.example.android_3d_loader.core.model.obj.ReaderOBJ;
import com.example.android_3d_loader.core.model.stl.ReaderSTL;
import com.google.gson.annotations.Expose;

public class ModelLoader {
    enum Format{
        OJB,
        STL,
        FBX
    }

    private static final String TAG = "ModelLoader";
    private Context context;
    @Expose
    private String path;
    private Format format;
    private ModelReader modelReader;
    private Model model;

    public ModelLoader(Context context, String path) {
        this.context = context;
        this.path = path;

    }

    private void checkType(){
        String format = path.substring(path.indexOf(".")).toLowerCase();
        switch (format){
            case ".obj":
                modelReader = new ReaderOBJ(context, path);
                this.format = Format.OJB;
                break;
            case ".stl":
                modelReader = new ReaderSTL(context, path);
                this.format = Format.STL;
                break;
            default:
                Toast.makeText(context, "Format \""+format+"\" not supported!", Toast.LENGTH_SHORT).show();
        }
    }

    public Model parse() {
        checkType();
        try {
            model = modelReader.parse();
            model.setModelPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            path = EngineConstants.DEFAULT_MODEL_PATH;
            parse();
        }
        return model;
    }

    public Format getFormat() {
        return format;
    }

    public Model getModel(){
        return model;
    }
}
