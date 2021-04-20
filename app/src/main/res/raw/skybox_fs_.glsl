#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 HDRColor;

uniform samplerCube CubeMap;
uniform bool IsHDRI;
uniform float Exposure;

in vec3 TexCoords;

const float Gamma = 2.2;

vec3 ACESToneMapping(vec3 source);
void main(){
    if(IsHDRI){
        vec3 sourceEnvMapColor = textureLod(CubeMap, TexCoords, 0.0).rgb;
        vec3 result = sourceEnvMapColor;
//        result = ACESToneMapping(result);
        float colorBrightness = dot(result, vec3(0.2126, 0.7152, 0.0722));
        if(colorBrightness > 1.0){
            HDRColor = vec4(result, 1.0);
        }
        FragColor = vec4(sourceEnvMapColor, 1.0);
    }else{
        FragColor = texture(CubeMap, TexCoords);
    }
}
vec3 ACESToneMapping(vec3 source){
    float A = 2.51f;
    float B = 0.03f;
    float C = 2.43f;
    float D = 0.59f;
    float E = 0.14f;

    source *= Exposure;
    return (source * (A * source + B)) / (source * (C * source + D) + E);
}
