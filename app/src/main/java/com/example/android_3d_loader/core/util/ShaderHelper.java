package com.example.android_3d_loader.core.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import static android.opengl.GLES32.GL_COMPILE_STATUS;
import static android.opengl.GLES32.GL_FRAGMENT_SHADER;
import static android.opengl.GLES32.GL_LINK_STATUS;
import static android.opengl.GLES32.GL_VALIDATE_STATUS;
import static android.opengl.GLES32.GL_VERTEX_SHADER;
import static android.opengl.GLES32.glAttachShader;
import static android.opengl.GLES32.glCompileShader;
import static android.opengl.GLES32.glCreateProgram;
import static android.opengl.GLES32.glCreateShader;
import static android.opengl.GLES32.glDeleteProgram;
import static android.opengl.GLES32.glDeleteShader;
import static android.opengl.GLES32.glGetProgramInfoLog;
import static android.opengl.GLES32.glGetProgramiv;
import static android.opengl.GLES32.glGetShaderInfoLog;
import static android.opengl.GLES32.glGetShaderiv;
import static android.opengl.GLES32.glLinkProgram;
import static android.opengl.GLES32.glShaderSource;
import static android.opengl.GLES32.glValidateProgram;

/**
 * 用于创建着色器对象
 */
public class ShaderHelper {

    private final static String TAG = "ShaderHelper";

    /**
     * 创建顶点着色器
     * @param shaderCode
     * @return
     */
    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 创建几何着色器
     * @param shaderCode
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int compileGeometryShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 创建片段着色器
     * @param shaderCode
     * @return
     */
    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode){

        /**
         * 创建新的着色器对象，并会返回该对象Id，即该对象的引用
         */
        final int shaderObjectId = glCreateShader(type);

        /**
         * 如果着色器对象的Id为0，说明创建未成功
         */
        if(shaderObjectId == 0){
            if(LoggerConfig.ON){
                Log.w(TAG, "无法创建新的着色器。");
            }
            return 0;
        }

        /**
         * 将着色器源代码上传到先前新创建的着色器对象里
         */
        glShaderSource(shaderObjectId, shaderCode);

        /**
         * 编译先前上传到着色器的源代码
         */
        glCompileShader(shaderObjectId);

        /**
         * 获取编译结果，并将结果值写入compileStatus的第0个元素
         */
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        /**
         * 取出着色器信息日志
         */
        if(LoggerConfig.ON){
//            Log.v(TAG, "\n着色器的编译状态：" + compileStatus[0] + "\n着色器源码：\n" + shaderCode + "\n的编译结果：" + glGetShaderInfoLog(shaderObjectId));
            Log.v(TAG, "\n"+(type==GL_VERTEX_SHADER?"顶点":"片段")+"着色器的编译状态：" + compileStatus[0] + "，编译结果：\n" + glGetShaderInfoLog(shaderObjectId));
        }

        /**
         * 验证编译状态并返回着色器对象Id，如果着色器对象Id为0，提示编译失败并删除该着色器对象
         */
        if(compileStatus[0] == 0){
            glDeleteShader(shaderObjectId);

            if(LoggerConfig.ON){
                Log.w(TAG, "着色器编译失败。");
            }

            return 0;
        }

        return shaderObjectId;
    }

    /**
     * 将顶点着色器和片段着色器进行链接，并返回进行链接的program对象的Id
     * @param vertexShaderId
     * @param fragmentShaderId
     * @return
     */
    public static int linkProgram(Integer vertexShaderId, Integer geometryShaderId, Integer fragmentShaderId){
        /**
         * 新建program对象
         */
        final int programObjectId = glCreateProgram();
        if(programObjectId == 0){
            if(LoggerConfig.ON){
                Log.w(TAG, "无法创建新的program对象");
            }
            return 0;
        }

        /**
         * 将顶点着色器和片段着色器附加到program对象上
         */
        glAttachShader(programObjectId, vertexShaderId);
        if (null != geometryShaderId){
            glAttachShader(programObjectId, geometryShaderId);
        }
        glAttachShader(programObjectId, fragmentShaderId);

        /**
         * 让program对象进行链接，并获取链接状态
         */
        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        /**
         * 检验链接状态并返回program对象的Id
         */
        if(linkStatus[0] == 0){
            glDeleteProgram(programObjectId);
            if(LoggerConfig.ON){
                Log.w(TAG, "program对象链接失败");
            }
            return 0;
        }
        return programObjectId;
    }

    /**
     * 检验program
     * @param programObjectId
     * @return
     */
    public static boolean validateProgram(int programObjectId){

        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Program对象的检验结果：" + validateStatus[0] + "，检验日志：\n" + glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }

    /**
     * 建立Program
     * @param vertexShaderSource 顶点着色器源代码
     * @param fragmentShaderSource 片段着色器源代码
     * @return Program的位置
     */
    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource){
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        int program = linkProgram(vertexShader, null,  fragmentShader);

        if(LoggerConfig.ON){
            validateProgram(program);
        }

        return program;
    }

    public static int buildProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId){

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId);

        return buildProgram(vertexShaderSource, fragmentShaderSource);
    }
}
