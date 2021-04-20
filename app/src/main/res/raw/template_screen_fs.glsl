#version 300 es
precision mediump float;
out vec4 FragColor;

in vec3 FragPos;
in vec2 TexCoords;

void main(){
    vec3 result = vec3(0.5);
    FragColor = vec4(result, 1.0);
}
