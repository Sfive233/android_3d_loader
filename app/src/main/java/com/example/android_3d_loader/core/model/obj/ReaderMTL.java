package com.example.android_3d_loader.core.model.obj;

import android.content.Context;

import com.example.android_3d_loader.core.texture.Texture;
import com.example.android_3d_loader.core.exception.TextureNotFoundException;
import com.example.android_3d_loader.core.texture.texture2D.Texture2D;
import com.example.android_3d_loader.view.widget.notice.Notice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReaderMTL {
    protected final String NEW_MTL = "newmtl";
    protected final String NS = "Ns";
    protected final String KA = "Ka";
    protected final String KD = "Kd";
    protected final String KS = "Ks";
    protected final String KE = "Ke";
    protected final String NI = "Ni";
    protected final String D = "d";
    protected final String TR = "Tr";
    protected final String TF = "Tf";
    protected final String ILLUM = "illum";
    protected final String MAP_KA = "map_Ka";
    protected final String MAP_KD = "map_Kd";
    protected final String MAP_KS = "map_Ks";
    protected final String MAP_BUMP = "map_Bump";
    protected final String MAP_TR = "map_Tr";
    protected final String MAP_D = "map_d";
    protected final String DISP = "disp";

    private Context context;
    private String directory;
    private String fileName;
    private String path;
    public ReaderMTL(Context context, String directory, String fileName){
        this.context = context;
        this.directory = directory;
        this.fileName = fileName;
        this.path = directory + "/" + fileName;
    }

    public HashMap<String, MTL> parse() throws TextureNotFoundException {
        List<MTL> mtlList = new ArrayList<>();
        int currentMTLIndex = 0;
        boolean isFirst = true;
        String sourceStr;
        String fileName;

        try {
//            InputStream inputStream = context.getAssets().open(path);
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            BufferedReader bufferedReader;
            if (path.startsWith("/")){
                File file = new File(path);
                FileReader fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
            }else {
                InputStream inputStream = context.getAssets().open(path);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
            }

            String currentLine;
            while (((currentLine = bufferedReader.readLine()) != null)) {
                if (currentLine.length() == 0 || currentLine.charAt(0) == '#') {
                    continue;
                }
                String[] subStr = currentLine.trim().split("\\s+");
                if (subStr.length == 0) {
                    continue;
                }
                Texture2D tempTexture;
                switch (subStr[0]){
                    case NEW_MTL:
                        if (isFirst){
                            isFirst = false;
                        }else {
                            currentMTLIndex++;
                        }
                        mtlList.add(new MTL(subStr[1]));
                        break;
                    case NS:
                        mtlList.get(currentMTLIndex).setNs(Float.parseFloat(subStr[1]));
                        break;
                    case KA:
                        mtlList.get(currentMTLIndex).setKa(new float[]{
                                Float.parseFloat(subStr[1]),
                                Float.parseFloat(subStr[2]),
                                Float.parseFloat(subStr[3])
                        });
                        break;
                    case KD:
                        mtlList.get(currentMTLIndex).setKd(new float[]{
                                Float.parseFloat(subStr[1]),
                                Float.parseFloat(subStr[2]),
                                Float.parseFloat(subStr[3])
                        });
                        break;
                    case KS:
                        mtlList.get(currentMTLIndex).setKs(new float[]{
                                Float.parseFloat(subStr[1]),
                                Float.parseFloat(subStr[2]),
                                Float.parseFloat(subStr[3])
                        });
                        break;
                    case KE:
                        mtlList.get(currentMTLIndex).setKe(new float[]{
                                Float.parseFloat(subStr[1]),
                                Float.parseFloat(subStr[2]),
                                Float.parseFloat(subStr[3])
                        });
                        break;
                    case NI:
                        mtlList.get(currentMTLIndex).setNi(Float.parseFloat(subStr[1]));
                        break;
                    case D:
                        mtlList.get(currentMTLIndex).setD(Float.parseFloat(subStr[1]));
                        break;
                    case TR:
                        mtlList.get(currentMTLIndex).setTr(Float.parseFloat(subStr[1]));
                        break;
                    case TF:
                        mtlList.get(currentMTLIndex).setTf(Float.parseFloat(subStr[1]));
                        break;
                    case ILLUM:
                        mtlList.get(currentMTLIndex).setIllum(Integer.parseInt(subStr[1]));
                        break;
                    case MAP_KA:
                        tempTexture = new Texture2D(context, directory + "/" + subStr[1]);
                        mtlList.get(currentMTLIndex).setMap_Ka(tempTexture.getTex() == 1? Texture2D.getNullTexture2D(): tempTexture);
                        break;
                    case MAP_KD:
                        sourceStr = subStr[1];// todo 优化代码
                        for (int i = 2; i < subStr.length; i++){
                            sourceStr += " "+subStr[i];
                        }
                        sourceStr = sourceStr.replaceAll("\\\\\\\\", "/");
                        sourceStr = sourceStr.replaceAll("\\\\", "/");
                        fileName = sourceStr.substring(sourceStr.lastIndexOf("/")+1);
                        tempTexture = new Texture2D(context, directory + "/" + fileName);
                        mtlList.get(currentMTLIndex).setMap_Kd(tempTexture.getTex() == 1? Texture2D.getNullTexture2D(): tempTexture);
                        break;
                    case MAP_KS:
                        sourceStr = subStr[1];
                        for (int i = 2; i < subStr.length; i++){
                            sourceStr += " "+subStr[i];
                        }
                        sourceStr = sourceStr.replaceAll("\\\\\\\\", "/");
                        sourceStr = sourceStr.replaceAll("\\\\", "/");
                        fileName = sourceStr.substring(sourceStr.lastIndexOf("/")+1);
                        tempTexture = new Texture2D(context, directory + "/" + fileName);
                        mtlList.get(currentMTLIndex).setMap_Ks(tempTexture.getTex() == 1? Texture2D.getNullTexture2D(): tempTexture);
                        break;
                    case MAP_BUMP:
                        sourceStr = subStr[1];
                        for (int i = 2; i < subStr.length; i++){
                            sourceStr += " "+subStr[i];
                        }
                        sourceStr = sourceStr.replaceAll("\\\\\\\\", "/");
                        sourceStr = sourceStr.replaceAll("\\\\", "/");
                        fileName = sourceStr.substring(sourceStr.lastIndexOf("/")+1);
                        tempTexture = new Texture2D(context, directory + "/" + fileName);
                        mtlList.get(currentMTLIndex).setMap_Bump(tempTexture.getTex() == 1? Texture2D.getNullTexture2D(): tempTexture);
                        break;
                    case MAP_TR:
                        tempTexture = new Texture2D(context, directory + "/" + subStr[1]);
                        mtlList.get(currentMTLIndex).setMap_Tr(tempTexture.getTex() == 1? Texture2D.getNullTexture2D(): tempTexture);
                        break;
                    case MAP_D:
                        tempTexture = new Texture2D(context, directory + "/" + subStr[1]);
                        mtlList.get(currentMTLIndex).setMap_d(tempTexture.getTex() == 1? Texture2D.getNullTexture2D(): tempTexture);
                        break;
                    case DISP:
                        tempTexture = new Texture2D(context, directory + "/" + subStr[1]);
                        mtlList.get(currentMTLIndex).setDisp(tempTexture.getTex() == 1? Texture2D.getNullTexture2D(): tempTexture);
                        break;
                }
            }
            bufferedReader.close();
//            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
//            Notice.makeWarning("MTL File \"" + path + "\" not found");
            return null;
        }

        HashMap<String, MTL> mtlHashMap = new HashMap<>();
        for (MTL mtl: mtlList){
            mtlHashMap.put(mtl.getNewmtl(), mtl);
        }


        mtlHashMap.put("default", new MTL("default"));
        return mtlHashMap;
    }
}
