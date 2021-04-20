package com.example.android_3d_loader.controller.process;

import android.content.Context;

import com.example.android_3d_loader.core.EngineConstants;
import com.example.android_3d_loader.core.model.mesh.basicObject.Plane;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.core.model.ModelLoader;

public class LoadModelProcess extends Process {


    public LoadModelProcess(Context context, OnProcessFinishedListener listener, Object... params) {
        super(context, listener, params);
    }

    @Override
    protected Object[] onProcess() {
        Model model;
        Plane plane;
        if (glCommunicator.isSaveExist()){
            // model
            Model savedModel = (Model) glCommunicator.getData("Model");
            model = new ModelLoader(getContext(), savedModel.getModelPath()).parse();
            model.createWireFrameMesh();
            glCommunicator.putData("Model", model);

            // plane
            plane = new Plane(model.getLongestSide() <= 1 ? 2: (int) model.getLongestSide() * 2);
            Plane savedPlane = (Plane) glCommunicator.getData("Plane");
            if (savedPlane != null){
                plane.setIsVisible(savedModel.getIsVisible().getVal());
            }
        }else {
            // model
            model = new ModelLoader(context, EngineConstants.DEFAULT_MODEL_PATH).parse();
            model.createWireFrameMesh();
            glCommunicator.putData("Model", model);

            // plane
            plane = new Plane(model.getLongestSide() <= 1 ? 2: (int) model.getLongestSide() * 2);
        }
        glCommunicator.putData("Plane", plane);

        return new Object[]{
                model, plane
        };
    }
}
