#version 300 es
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNormal;

uniform mat4 Model;
uniform mat4 View;
uniform mat4 Projection;

out vec3 TexCoords;

void main(){
    TexCoords = aPos;

    mat4 rotView = mat4(mat3(View));
    vec4 newPosition = Projection * rotView * vec4(aPos, 1.0f);
    gl_Position = newPosition.xyww;
}
