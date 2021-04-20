#version 300 es
layout (location = 0) in vec3 aPos;

out vec2 TexCoords;

void main(){
    TexCoords = vec2(aPos.x / 2.0 + 0.5, aPos.y / 2.0 + 0.5);
    gl_Position = vec4(aPos, 1.0);
}
