package com.example.android_3d_loader.core.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelReader {
    protected static String TAG = "ModelReader";
    protected Context context;
    protected String path;
    protected String directory;
    protected String modelName;
    protected List<Float> vertices;
    protected List<Float> texcoords;
    protected List<Float> normals;

    public abstract Model parse() throws Exception;

    public ModelReader(Context context, String path){
        this.context = context;
        this.path = path;
        this.directory = path.substring(0, path.lastIndexOf("/"));
        this.modelName = path.substring(path.lastIndexOf("/")+1);

        this.vertices = new ArrayList<>();
        this.texcoords = new ArrayList<>();
        this.normals = new ArrayList<>();
    }
}
