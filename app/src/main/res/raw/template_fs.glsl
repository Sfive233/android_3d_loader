#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;

uniform vec3 Color;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 Normal;

void main(){
    FragColor = vec4(FragPos, 1.0);
}
