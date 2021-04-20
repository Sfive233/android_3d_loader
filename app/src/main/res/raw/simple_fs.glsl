#version 300 es
precision mediump float;
out vec4 FragColor;

uniform vec3 Color;

in vec3 FragPos;
in vec3 Normal;

void main(){
    FragColor = vec4(Color, 1.0);
}
