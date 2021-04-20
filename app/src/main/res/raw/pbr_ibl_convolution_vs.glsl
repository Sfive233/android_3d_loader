#version 300 es
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNormal;

uniform mat4 Model;
uniform mat4 View;
uniform mat4 Projection;

out vec3 LocalPos;

out vec2 Texcoords;

void main(){
    LocalPos = aPos;
    gl_Position = Projection * View * vec4(aPos, 1.0);

    Texcoords = aTexCoord;
}
