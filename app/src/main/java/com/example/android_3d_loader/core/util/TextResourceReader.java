package com.example.android_3d_loader.core.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 用于加载glsl文件
 */
public class TextResourceReader {

    /**
     * 读取并返回glsl文件内的着色器源代码
     * @param context
     * @param resourceId
     * @return
     */
    public static String readTextFileFromResource(Context context, int resourceId){
        StringBuilder body = new StringBuilder();
        String resourceName = context.getResources().getResourceName(resourceId);
        try {
            Log.d("TextReader", "正在打开该资源: "+resourceName);
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while (((nextLine = bufferedReader.readLine()) != null)) {
                body.append(nextLine);
                body.append('\n');
            }
        }catch (IOException e){
            throw new RuntimeException("无法打开该资源："+resourceName, e);
        }catch (Resources.NotFoundException nfe){
            throw new RuntimeException("资源未找到："+resourceName, nfe);
        }

//        Log.v("glsl" , "读取结果：\n" + body.toString());

        return body.toString();
    }
}
