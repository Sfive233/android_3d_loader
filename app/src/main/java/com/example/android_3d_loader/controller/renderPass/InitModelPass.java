package com.example.android_3d_loader.controller.renderPass;

import com.example.android_3d_loader.controller.communicator.GLCommunicator;
import com.example.android_3d_loader.core.EngineConstants;
import com.example.android_3d_loader.core.model.ModelLoader;
import com.example.android_3d_loader.core.model.mesh.basicObject.Plane;
import com.example.android_3d_loader.core.model.Model;
import com.example.android_3d_loader.controller.process.LoadModelProcess;
import com.example.android_3d_loader.controller.process.Process;

public class InitModelPass extends BaseRenderPass{

    protected GLCommunicator glCommunicator;
    private Model model;
    private Plane plane;
    protected LoadModelProcess loadModelProcess;

    @Override
    public void renderInit() {
        glCommunicator = GLCommunicator.getInstance();
        loadModelProcess = new LoadModelProcess(
                getContext(),
                new Process.OnProcessFinishedListener() {
                    @Override
                    public void onFinish(Object... objects) {
                        model = (Model) objects[0];
                        plane = (Plane) objects[1];
                    }
                }
        );
    }

    @Override
    public void renderReact() {
//        if (glCommunicator.isSaveExist()){
//            // model
//            Model savedModel = (Model) glCommunicator.getData("Model");
//            model = new ModelLoader(getContext(), savedModel.getModelPath()).parse();
//            model.createWireFrameMesh();
//            glCommunicator.putData("Model", model);
//
//            // plane
//            plane = new Plane(model.getLongestSide() <= 1 ? 2: (int) model.getLongestSide() * 2);
//            Plane savedPlane = (Plane) glCommunicator.getData("Plane");
//            if (savedPlane != null){
//                plane.setIsVisible(savedModel.getIsVisible().getVal());
//            }
//        }else {
//            // model
//            model = new ModelLoader(context, EngineConstants.DEFAULT_MODEL_PATH).parse();
//            model.createWireFrameMesh();
//            glCommunicator.putData("Model", model);
//
//            // plane
//            plane = new Plane(model.getLongestSide() <= 1 ? 2: (int) model.getLongestSide() * 2);
//        }
//        glCommunicator.putData("Plane", plane);
        loadModelProcess.doProcess();
    }

    @Override
    public Object[] renderUpdate(Object... inputs) {
        return new Object[]{
                model, plane
        };
    }
}
