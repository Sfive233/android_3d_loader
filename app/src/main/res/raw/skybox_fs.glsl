#version 300 es
precision highp float;
layout (location = 0) out vec3 FragColor;
layout (location = 1) out vec3 BrightColor;

uniform samplerCube CubeMap;
uniform float CubeMapLod;

in vec3 TexCoords;
in vec3 FragPos;

void main(){
    vec3 result = textureLod(CubeMap, TexCoords, CubeMapLod).rgb;

//    result = result / (result + vec3(1.0f));

    FragColor = result;
    BrightColor = vec3(0.0);
}
