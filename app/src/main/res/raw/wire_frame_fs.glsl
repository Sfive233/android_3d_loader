#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 HDRColor;

uniform vec3 Color;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 Normal;

void main(){
    FragColor = vec4(Color, 1.0);
    HDRColor = vec4(0.0);
}
